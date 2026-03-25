package com.germandebustamante.ringtonemanager.ui.screen.home

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import com.germandebustamante.ringtonemanager.core.model.error.ErrorBO
import com.germandebustamante.ringtonemanager.core.model.ringtone.RingtoneBO
import com.germandebustamante.ringtonemanager.core.model.ringtone.RingtoneBOMother
import com.germandebustamante.ringtonemanager.core.navigation.action.Navigator
import com.germandebustamante.ringtonemanager.core.navigation.destination.Destination
import com.germandebustamante.ringtonemanager.domain.ringtone.usecase.GetPopularRingtonesUseCase
import com.germandebustamante.ringtonemanager.utils.audio.MultiplePlayerAdapter
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MockKExtension::class)
class HomeViewModelTest {

    private lateinit var sut: HomeViewModel

    @MockK
    private lateinit var getPopularRingtonesUseCase: GetPopularRingtonesUseCase

    @MockK
    private lateinit var player: MultiplePlayerAdapter

    @MockK
    private lateinit var navigator: Navigator

    private val testDispatcher = UnconfinedTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        givenViewModelInitSuccessDependencies()
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Nested
    inner class Init {

        @Test
        fun `GIVEN successful dependencies WHEN init THEN state is updated correctly`() = runTest {
            // Given
            givenViewModelInitSuccessDependencies()

            // When
            buildSut()

            // Then
            sut.state.test {
                val state = awaitItem()
                assertTrue(state.ringtones.isNotEmpty())
                assertFalse(state.isLoading)
            }
        }

        @Test
        fun `GIVEN successful dependencies WHEN init THEN media items are added to the player`() = runTest {
            // Given
            givenViewModelInitSuccessDependencies()

            // When
            buildSut()

            // Then
            coVerify(exactly = 1) { player.addMediaItems(any()) }
        }

        @Test
        fun `GIVEN successful dependencies WHEN init THEN error state is null`() = runTest {
            // Given
            givenViewModelInitSuccessDependencies()

            // When
            buildSut()

            // Then
            sut.state.test {
                val state = awaitItem()
                assertNull(state.error)
            }
        }

        @Test
        fun `GIVEN successful dependencies WHEN init THEN ringtones list is populated`() = runTest {
            // Given
            val expectedRingtones = RingtoneBOMother.randomList()
            givenViewModelInitSuccessDependencies(expectedRingtones)

            // When
            buildSut()

            // Then
            sut.state.test {
                val state = awaitItem()
                assertEquals(expectedRingtones, state.ringtones)
            }
        }

        @Test
        fun `GIVEN getPopularRingtonesUseCase fails WHEN init THEN error state is updated correctly`() = runTest {
            // Given
            givenGetPopularRingtoneFailedUseCase()

            // When
            buildSut()

            // Then
            sut.state.test {
                val errorState = awaitItem() // State after error
                assertTrue(errorState.error is ErrorBO.NotFound)
                assertFalse(errorState.isLoading)
            }
        }

        //region Stubs
        private fun givenGetPopularRingtoneFailedUseCase() {
            every { getPopularRingtonesUseCase() } returns flowOf(ErrorBO.NotFound.left())
            every { player.addMediaItems(any()) } just Runs
        }
        //endregion
    }

    @Nested
    inner class OnRingtoneClicked {

        @BeforeEach
        fun setup() {
            buildSut()
        }

        @Test
        fun `GIVEN user is on home screen WHEN user clicks on ringtone THEN navigates to ringtone detail`() = runTest {
            // Given
            givenNavigatorActions()
            val ringtoneIdDestination = RingtoneBOMother.random().id

            // When
            sut.navigateToRingtoneDetail(ringtoneIdDestination)

            // Then
            coVerify(exactly = 1) { navigator.navigate(Destination.RingtoneDetailScreen(ringtoneIdDestination)) }
        }
    }

    @Nested
    inner class OnPlayRingtoneClicked {

        @BeforeEach
        fun setup() {
            buildSut()
        }

        @Test
        fun `GIVEN ringtone is stopped WHEN user clicks play button THEN ringtone starts playing`() = runTest {
            // Given
            givenPlayerPlay()
            val ringtone = RingtoneBOMother.random()

            // When
            sut.onPlayRingtoneClicked(ringtone, isPlaying = false)

            // Then
            coVerify(exactly = 1) { player.play(ringtone.fileUrl) }
        }

        @Test
        fun `GIVEN ringtone is stopped WHEN user clicks play button THEN state is updated with current ringtone id`() = runTest {
            // Given
            givenPlayerPlay()
            val ringtone = RingtoneBOMother.random()

            // When
            sut.onPlayRingtoneClicked(ringtone, isPlaying = false)

            // Then
            sut.state.test {
                val state = awaitItem()
                assertEquals(ringtone.id, state.currentRingtonePlayingId)
            }
        }

        @Test
        fun `GIVEN ringtone is playing WHEN user clicks pause button THEN ringtone is paused`() = runTest {
            // Given
            givenPlayerPause()
            val ringtone = RingtoneBOMother.random()

            // When
            sut.onPlayRingtoneClicked(ringtone, isPlaying = true)

            // Then
            coVerify(exactly = 1) { player.pause() }
        }

        @Test
        fun `GIVEN ringtone is playing WHEN user clicks pause button THEN current ringtone id is cleared`() = runTest {
            // Given
            givenPlayerPlay()
            givenPlayerPause()
            val ringtone = RingtoneBOMother.random()
            sut.onPlayRingtoneClicked(ringtone, isPlaying = false)

            // When
            sut.onPlayRingtoneClicked(ringtone, isPlaying = true)

            // Then
            sut.state.test {
                val state = awaitItem()
                assertNull(state.currentRingtonePlayingId)
            }
        }

        //region Stubs
        private fun givenPlayerPlay() {
            every { player.play(any()) } just Runs
        }

        private fun givenPlayerPause() {
            every { player.pause() } just Runs
        }
        //endregion
    }

    @Nested
    inner class PlayerLifecycle {

        @BeforeEach
        fun setup() {
            buildSut()
        }

        @Test
        fun `GIVEN app is running WHEN user moves app to background THEN player resources are released`() = runTest {
            // Given
            every { player.release() } just Runs

            // When
            sut.releasePlayer()

            // Then
            coVerify(exactly = 1) { player.release() }
        }

        @Test
        fun `GIVEN app is in background WHEN user returns to app THEN player is restored`() = runTest {
            // Given
            every { player.restore() } just Runs

            // When
            sut.restorePlayer()

            // Then
            coVerify(exactly = 1) { player.restore() }
        }

        @Test
        fun `GIVEN ringtone is playing WHEN user pauses player THEN player is paused`() = runTest {
            // Given
            every { player.pause() } just Runs

            // When
            sut.pausePlayer()

            // Then
            coVerify(exactly = 1) { player.pause() }
        }

        @Test
        fun `GIVEN ringtone is playing WHEN user pauses player THEN current ringtone id is cleared`() = runTest {
            // Given
            every { player.pause() } just Runs
            every { player.play(any()) } just Runs
            val ringtone = RingtoneBOMother.random()
            sut.onPlayRingtoneClicked(ringtone, isPlaying = false)

            // When
            sut.pausePlayer()

            // Then
            sut.state.test {
                val state = awaitItem()
                assertNull(state.currentRingtonePlayingId)
            }
        }
    }

    //region General Stubs
    private fun givenViewModelInitSuccessDependencies(ringtoneList: List<RingtoneBO> = RingtoneBOMother.randomList()) {
        every { getPopularRingtonesUseCase() } returns flowOf(ringtoneList.right())
        every { player.addMediaItems(any()) } just Runs
    }

    private fun buildSut() {
        sut = HomeViewModel(
            getPopularRingtonesUseCase = getPopularRingtonesUseCase,
            player = player,
            navigator = navigator,
        )
    }

    private fun givenNavigatorActions() {
        coEvery { navigator.navigate(any()) } just Runs
    }
    //endregion
}