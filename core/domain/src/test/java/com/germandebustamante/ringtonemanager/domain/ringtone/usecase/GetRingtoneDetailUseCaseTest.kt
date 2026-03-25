package com.germandebustamante.ringtonemanager.domain.ringtone.usecase

import arrow.core.left
import arrow.core.right
import com.germandebustamante.ringtonemanager.core.model.error.ErrorBOMother
import com.germandebustamante.ringtonemanager.core.model.ringtone.RingtoneBOMother
import com.germandebustamante.ringtonemanager.domain.ringtone.repository.AnalyticsRepository
import com.germandebustamante.ringtonemanager.domain.ringtone.repository.RingtoneItemRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.Runs
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class GetRingtoneDetailUseCaseTest {

    private lateinit var sut: GetRingtoneDetailUseCase

    @MockK
    private lateinit var ringtoneItemRepository: RingtoneItemRepository

    @MockK
    private lateinit var analyticsRepository: AnalyticsRepository

    @BeforeEach
    fun setUp() {
        sut = GetRingtoneDetailUseCase(ringtoneItemRepository, analyticsRepository)
    }

    @Test
    fun `GIVEN ringtone exists WHEN getting detail THEN returns success`() = runTest {
        // Given
        val ringtone = RingtoneBOMother.random()
        coEvery { ringtoneItemRepository.getRingtoneDetail(ringtone.id) } returns ringtone.right()
        every { analyticsRepository.onRingtoneObtained(any(), any()) } just Runs

        // When
        val result = sut(ringtone.id)

        // Then
        assertTrue(result.isRight())
        assertEquals(ringtone, result.getOrNull())
    }

    @Test
    fun `GIVEN ringtone exists WHEN getting detail THEN analytics is called`() = runTest {
        // Given
        val ringtone = RingtoneBOMother.random()
        coEvery { ringtoneItemRepository.getRingtoneDetail(ringtone.id) } returns ringtone.right()
        every { analyticsRepository.onRingtoneObtained(any(), any()) } just Runs

        // When
        sut(ringtone.id)

        // Then
        coVerify(exactly = 1) { analyticsRepository.onRingtoneObtained(ringtone.id, ringtone.name) }
    }

    @Test
    fun `GIVEN server error WHEN getting detail THEN returns error`() = runTest {
        // Given
        val error = ErrorBOMother.serverError()
        coEvery { ringtoneItemRepository.getRingtoneDetail(any()) } returns error.left()

        // When
        val result = sut("ringtone_1")

        // Then
        assertTrue(result.isLeft())
        assertEquals(error, result.leftOrNull())
    }

    @Test
    fun `GIVEN server error WHEN getting detail THEN analytics is NOT called`() = runTest {
        // Given
        coEvery { ringtoneItemRepository.getRingtoneDetail(any()) } returns ErrorBOMother.serverError().left()

        // When
        sut("ringtone_1")

        // Then
        coVerify(exactly = 0) { analyticsRepository.onRingtoneObtained(any(), any()) }
    }
}
