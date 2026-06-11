package com.germandebustamante.ringtonemanager.domain.authorization.usecase

import arrow.core.left
import arrow.core.right
import com.germandebustamante.ringtonemanager.core.model.authorization.LoginTypeBO
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
class SignInUserUseCaseTest {

    private lateinit var sut: SignInUserUseCase

    @MockK
    private lateinit var authenticationRepository: AuthenticationRepository

    @BeforeEach
    fun setUp() {
        sut = SignInUserUseCase(authenticationRepository)
    }

    @Test
    fun `GIVEN sign in success WHEN invoked THEN returns success and delegates to repository`() = runTest {
        // Given
        val loginType = LoginTypeBO.Default(EMAIL, PASSWORD)
        coEvery { authenticationRepository.signIn(loginType) } returns Unit.right()

        // When
        val result = sut(loginType)

        // Then
        assertTrue(result.isRight())
        coVerify(exactly = 1) { authenticationRepository.signIn(loginType) }
    }

    @Test
    fun `GIVEN invalid credentials WHEN invoked THEN returns error`() = runTest {
        // Given
        val loginType = LoginTypeBO.Default(EMAIL, PASSWORD)
        val error = ErrorBOMother.invalidCredentials()
        coEvery { authenticationRepository.signIn(loginType) } returns error.left()

        // When
        val result = sut(loginType)

        // Then
        assertTrue(result.isLeft())
        assertEquals(error, result.leftOrNull())
    }

    @Test
    fun `GIVEN google login type WHEN invoked THEN delegates to repository`() = runTest {
        // Given
        val loginType = LoginTypeBO.Google(GOOGLE_TOKEN)
        coEvery { authenticationRepository.signIn(loginType) } returns Unit.right()

        // When
        sut(loginType)

        // Then
        coVerify(exactly = 1) { authenticationRepository.signIn(loginType) }
    }

    companion object {
        private const val EMAIL = "test@example.com"
        private const val PASSWORD = "password123"
        private const val GOOGLE_TOKEN = "google_token"
    }
}
