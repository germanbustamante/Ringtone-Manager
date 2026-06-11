package com.germandebustamante.ringtonemanager.domain.authorization.usecase

import com.germandebustamante.ringtonemanager.domain.authorization.repository.AuthenticationRepository
import io.mockk.coVerify
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.Runs
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class SignOutUserUseCaseTest {

    private lateinit var sut: SignOutUserUseCase

    @MockK
    private lateinit var authenticationRepository: AuthenticationRepository

    @BeforeEach
    fun setUp() {
        sut = SignOutUserUseCase(authenticationRepository)
    }

    @Test
    fun `GIVEN use case WHEN invoked THEN delegates sign out to repository`() = runTest {
        // Given
        coEvery { authenticationRepository.signOut() } just Runs

        // When
        sut()

        // Then
        coVerify(exactly = 1) { authenticationRepository.signOut() }
    }
}
