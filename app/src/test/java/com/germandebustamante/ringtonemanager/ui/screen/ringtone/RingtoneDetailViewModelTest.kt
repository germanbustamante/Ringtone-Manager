package com.germandebustamante.ringtonemanager.ui.screen.ringtone

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import com.germandebustamante.ringtonemanager.core.model.error.ErrorBOMother
import com.germandebustamante.ringtonemanager.core.model.ringtone.RingtoneBOMother
import com.germandebustamante.ringtonemanager.core.navigation.action.Navigator
import com.germandebustamante.ringtonemanager.core.navigation.destination.Destination
import com.germandebustamante.ringtonemanager.domain.ringtone.usecase.GetRingtoneDetailUseCase
import com.germandebustamante.ringtonemanager.domain.ringtone.usecase.IncrementRingtonePopularityUseCase
import com.germandebustamante.ringtonemanager.utils.audio.SinglePlayerAdapter
import arrow.core.right
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.Runs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MockKExtension::class)
class RingtoneDetailViewModelTest {

    private lateinit var sut: RingtoneDetailViewModel

    @MockK
    private lateinit var fetchRingtoneDetailUseCase: GetRingtoneDetailUseCase

    @MockK
    private lateinit var incrementRingtonePopularityUseCase: IncrementRingtonePopularityUseCase

    @MockK(relaxed = true)
    private lateinit var playerAdapter: SinglePlayerAdapter

    @MockK(relaxed = true)
    private lateinit var navigator: Navigator

    private val route = Destination.RingtoneDetailScreen(ringtoneId = "ringtone_1")
    private val testDispatcher = UnconfinedTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        coEvery { incrementRingtonePopularityUseCase(any()) } returns Unit.right()
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `GIVEN ringtone exists WHEN fetching detail THEN state contains ringtone`() = runTest {
        // Given
        val ringtone = RingtoneBOMother.random()
        coEvery { fetchRingtoneDetailUseCase(route.ringtoneId) } returns ringtone.right()

        // When
        buildSut()

        // Then
        sut.uiState.test {
            val state = awaitItem()
            assertEquals(ringtone, state.ringtone)
            assertFalse(state.isLoading)
            assertNull(state.error)
        }
    }

    @Test
    fun `GIVEN ringtone exists WHEN fetching detail THEN popularity is incremented`() = runTest {
        // Given
        val ringtone = RingtoneBOMother.random()
        coEvery { fetchRingtoneDetailUseCase(route.ringtoneId) } returns ringtone.right()

        // When
        buildSut()

        // Then
        coVerify(exactly = 1) { incrementRingtonePopularityUseCase(ringtone.id) }
    }

    @Test
    fun `GIVEN server error WHEN fetching detail THEN popularity is NOT incremented`() = runTest {
        // Given
        coEvery { fetchRingtoneDetailUseCase(any()) } returns ErrorBOMother.serverError().left()

        // When
        buildSut()

        // Then
        coVerify(exactly = 0) { incrementRingtonePopularityUseCase(any()) }
    }

    @Test
    fun `GIVEN server error WHEN fetching detail THEN error state is set`() = runTest {
        // Given
        val error = ErrorBOMother.serverError()
        coEvery { fetchRingtoneDetailUseCase(any()) } returns error.left()

        // When
        buildSut()

        // Then
        sut.uiState.test {
            val state = awaitItem()
            assertNotNull(state.error)
            assertEquals(error, state.error)
            assertFalse(state.isLoading)
        }
    }

    @Test
    fun `GIVEN ringtone loaded WHEN play button clicked THEN isPlaying is true`() = runTest {
        // Given
        val ringtone = RingtoneBOMother.random()
        coEvery { fetchRingtoneDetailUseCase(any()) } returns ringtone.right()
        buildSut()

        // When
        sut.onPlayPauseRingtone()

        // Then
        assertTrue(sut.uiState.value.isPlaying)
    }

    @Test
    fun `GIVEN ringtone is playing WHEN pause button clicked THEN isPlaying is false`() = runTest {
        // Given
        val ringtone = RingtoneBOMother.random()
        coEvery { fetchRingtoneDetailUseCase(any()) } returns ringtone.right()
        buildSut()
        sut.onPlayPauseRingtone() // start playing

        // When
        sut.onPlayPauseRingtone() // pause

        // Then
        assertFalse(sut.uiState.value.isPlaying)
    }

    @Test
    fun `GIVEN playback WHEN updatePlaybackPosition called THEN position is updated`() = runTest {
        // Given
        coEvery { fetchRingtoneDetailUseCase(any()) } returns RingtoneBOMother.random().right()
        buildSut()

        // When
        sut.updatePlaybackPosition(5000)

        // Then
        assertEquals(5000, sut.uiState.value.currentPlaybackPosition)
    }

    private fun buildSut() {
        sut = RingtoneDetailViewModel(
            route = route,
            playerAdapter = playerAdapter,
            fetchRingtoneDetailUseCase = fetchRingtoneDetailUseCase,
            incrementRingtonePopularityUseCase = incrementRingtonePopularityUseCase,
            navigator = navigator,
        )
    }
}
