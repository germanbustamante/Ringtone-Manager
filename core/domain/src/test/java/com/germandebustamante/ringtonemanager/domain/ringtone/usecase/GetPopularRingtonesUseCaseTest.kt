package com.germandebustamante.ringtonemanager.domain.ringtone.usecase

import app.cash.turbine.test
import com.germandebustamante.ringtonemanager.core.model.ringtone.RingtoneBO
import com.germandebustamante.ringtonemanager.core.model.ringtone.RingtoneBOMother
import com.germandebustamante.ringtonemanager.domain.ringtone.repository.RingtoneListRepository
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
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
    fun `GIVEN popular ringtones success WHEN invoked THEN returns ringtones list`() = runTest {
        val expectedRingtones = RingtoneBOMother.randomList()
        every { ringtoneListRepository.popularRingtones } returns flowOf(expectedRingtones)

        val result = sut()

        result.test {
            assertEquals(expectedRingtones, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `GIVEN empty ringtones list WHEN invoked THEN returns empty list`() = runTest {
        every { ringtoneListRepository.popularRingtones } returns flowOf(emptyList<RingtoneBO>())

        val result = sut()

        result.test {
            assertTrue(awaitItem().isEmpty())
            awaitComplete()
        }
    }

    //region Stubs
    private fun givenRingtoneListRepositorySuccess(ringtones: List<RingtoneBO> = RingtoneBOMother.randomList()) {
        every { ringtoneListRepository.popularRingtones } returns flowOf(ringtones)
    }
    //endregion
}
