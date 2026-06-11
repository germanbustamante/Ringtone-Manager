package com.germandebustamante.ringtonemanager.domain.ringtone.usecase

import arrow.core.left
import arrow.core.right
import com.germandebustamante.ringtonemanager.core.model.error.ErrorBOMother
import com.germandebustamante.ringtonemanager.domain.ringtone.repository.RingtoneItemRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class IncrementRingtonePopularityUseCaseTest {

    private lateinit var sut: IncrementRingtonePopularityUseCase

    @MockK
    private lateinit var ringtoneItemRepository: RingtoneItemRepository

    @BeforeEach
    fun setUp() {
        sut = IncrementRingtonePopularityUseCase(ringtoneItemRepository)
    }

    @Test
    fun `GIVEN increment success WHEN invoked THEN returns success and delegates to repository`() = runTest {
        // Given
        coEvery { ringtoneItemRepository.incrementPopularity(RINGTONE_ID) } returns Unit.right()

        // When
        val result = sut(RINGTONE_ID)

        // Then
        assertTrue(result.isRight())
        coVerify(exactly = 1) { ringtoneItemRepository.incrementPopularity(RINGTONE_ID) }
    }

    @Test
    fun `GIVEN server error WHEN invoked THEN returns error`() = runTest {
        // Given
        val error = ErrorBOMother.serverError()
        coEvery { ringtoneItemRepository.incrementPopularity(RINGTONE_ID) } returns error.left()

        // When
        val result = sut(RINGTONE_ID)

        // Then
        assertTrue(result.isLeft())
        assertEquals(error, result.leftOrNull())
    }

    companion object {
        private const val RINGTONE_ID = "ringtone_123"
    }
}
