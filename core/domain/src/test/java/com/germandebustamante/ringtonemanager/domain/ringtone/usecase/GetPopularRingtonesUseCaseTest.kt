package com.germandebustamante.ringtonemanager.domain.ringtone.usecase

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import com.germandebustamante.ringtonemanager.core.model.error.ErrorBOMother
import com.germandebustamante.ringtonemanager.core.model.ringtone.RingtoneBO
import com.germandebustamante.ringtonemanager.core.model.ringtone.RingtoneBOMother
import com.germandebustamante.ringtonemanager.domain.ringtone.repository.RingtoneListRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class GetPopularRingtonesUseCaseTest {

    private lateinit var sut: GetPopularRingtonesUseCase

    @MockK
    private lateinit var ringtoneListRepository: RingtoneListRepository

    @BeforeEach
    fun setUp() {
        sut = GetPopularRingtonesUseCase(ringtoneListRepository)
    }

    @Test
    fun `GIVEN popular ringtones success WHEN invoked THEN returns success with ringtones list`() = runTest {
        // Given
        val expectedRingtones = RingtoneBOMother.randomList()
        givenRingtoneListRepositorySuccess(expectedRingtones)

        // When
        val result = sut()

        // Then
        result.test {
            val item = awaitItem()
            assert(item.isRight())
            assertEquals(expectedRingtones, item.getOrNull())
            awaitComplete()
        }
    }

    @Test
    fun `GIVEN empty ringtones list WHEN invoked THEN returns success with empty list`() = runTest {
        // Given
        every { ringtoneListRepository.popularRingtones } returns flowOf(emptyList<RingtoneBO>().right())

        // When
        val result = sut()

        // Then
        result.test {
            val item = awaitItem()
            assert(item.isRight())
            assert(item.getOrNull()?.isEmpty() == true)
            awaitComplete()
        }
    }

    @Test
    fun `GIVEN server error WHEN invoked THEN returns error`() = runTest {
        // Given
        givenRingtoneListRepositoryError()

        // When
        val result = sut()

        // Then
        result.test {
            val item = awaitItem()
            assert(item.isLeft())
            assertEquals(ErrorBOMother.serverError(), item.leftOrNull())
            awaitComplete()
        }
    }

    //region Stubs
    private fun givenRingtoneListRepositorySuccess(ringtones: List<RingtoneBO> = RingtoneBOMother.randomList()) {
        coEvery { ringtoneListRepository.popularRingtones  } returns flowOf(ringtones.right())
    }

    private fun givenRingtoneListRepositoryError() {
        coEvery { ringtoneListRepository.popularRingtones  } returns flowOf(ErrorBOMother.serverError().left())
    }
    //endregion
}
