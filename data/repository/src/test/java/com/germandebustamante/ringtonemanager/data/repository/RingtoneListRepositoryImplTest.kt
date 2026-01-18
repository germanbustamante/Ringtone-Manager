package com.germandebustamante.ringtonemanager.data.repository

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import com.germandebustamante.ringtonemanager.core.model.error.ErrorBOMother
import com.germandebustamante.ringtonemanager.core.model.ringtone.RingtoneBO
import com.germandebustamante.ringtonemanager.core.model.ringtone.RingtoneBOMother
import com.germandebustamante.ringtonemanager.data.datasource.RingtoneListRemoteDataSource
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MockKExtension::class)
class RingtoneListRepositoryImplTest {

    private lateinit var sut: RingtoneListRepositoryImpl

    @MockK
    private lateinit var remoteDataSource: RingtoneListRemoteDataSource

    private val testDispatcher = UnconfinedTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `GIVEN popular ringtones available WHEN accessing popular ringtones THEN returns success with ringtones`() = runTest {
        // Given
        val expectedRingtones = RingtoneBOMother.randomList()
        givenRemoteDataSourceSuccess(expectedRingtones)

        // When
        val result = sut.popularRingtones

        // Then
        result.test {
            val item = awaitItem()
            assert(item.isRight())
            assertEquals(expectedRingtones, item.getOrNull())
            awaitComplete()
        }
    }

    @Test
    fun `GIVEN empty ringtones list WHEN accessing popular ringtones THEN returns success with empty list`() = runTest {
        // Given
        every { remoteDataSource.getPopularRingtones() } returns flowOf(emptyList<RingtoneBO>().right())
        sut = RingtoneListRepositoryImpl(remoteDataSource)

        // When
        val result = sut.popularRingtones

        // Then
        result.test {
            val item = awaitItem()
            assert(item.isRight())
            assert(item.getOrNull()?.isEmpty() == true)
            awaitComplete()
        }
    }

    @Test
    fun `GIVEN server error WHEN accessing popular ringtones THEN returns error`() = runTest {
        // Given
        givenRemoteDataSourceError()

        // When
        val result = sut.popularRingtones

        // Then
        result.test {
            val item = awaitItem()
            assert(item.isLeft())
            assertEquals(ErrorBOMother.serverError(), item.leftOrNull())
            awaitComplete()
        }
    }

    //region Stubs
    private fun givenRemoteDataSourceSuccess(ringtones: List<RingtoneBO> = RingtoneBOMother.randomList()) {
        every { remoteDataSource.getPopularRingtones() } returns flowOf(ringtones.right())
        sut = RingtoneListRepositoryImpl(remoteDataSource)
    }

    private fun givenRemoteDataSourceError() {
        every { remoteDataSource.getPopularRingtones() } returns flowOf(ErrorBOMother.serverError().left())
        sut = RingtoneListRepositoryImpl(remoteDataSource)
    }
    //endregion
}
