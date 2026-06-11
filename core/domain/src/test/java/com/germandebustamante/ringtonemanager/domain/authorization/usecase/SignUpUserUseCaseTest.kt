package com.germandebustamante.ringtonemanager.domain.authorization.usecase

import arrow.core.left
import arrow.core.right
import com.germandebustamante.ringtonemanager.core.model.error.ErrorBOMother
import com.germandebustamante.ringtonemanager.domain.authorization.repository.AuthenticationRepository
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
class SignUpUserUseCaseTest {

    private lateinit var sut: SignUpUserUseCase

    @MockK
    private lateinit var authenticationRepository: AuthenticationRepository

    @BeforeEach
    fun setUp() {
        sut = SignUpUserUseCase(authenticationRepository)
    }

    @Test
    fun `GIVEN sign up success WHEN invoked THEN returns success and delegates to repository`() = runTest {
        // Given
        coEvery { authenticationRepository.signUp(EMAIL, PASSWORD, NAME) } returns Unit.right()

        // When
        val result = sut(EMAIL, PASSWORD, NAME)

        // Then
        assertTrue(result.isRight())
        coVerify(exactly = 1) { authenticationRepository.signUp(EMAIL, PASSWORD, NAME) }
    }

    @Test
    fun `GIVEN email already in use WHEN invoked THEN returns error`() = runTest {
        // Given
        val error = ErrorBOMother.emailAddressAlreadyInUse()
        coEvery { authenticationRepository.signUp(EMAIL, PASSWORD, NAME) } returns error.left()

        // When
        val result = sut(EMAIL, PASSWORD, NAME)

        // Then
        assertTrue(result.isLeft())
        assertEquals(error, result.leftOrNull())
    }

    companion object {
        private const val EMAIL = "test@example.com"
        private const val PASSWORD = "password123"
        private const val NAME = "Test User"
    }
}
