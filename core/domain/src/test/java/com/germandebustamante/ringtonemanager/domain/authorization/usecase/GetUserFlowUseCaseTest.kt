package com.germandebustamante.ringtonemanager.domain.authorization.usecase

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import com.germandebustamante.ringtonemanager.core.model.authorization.UserBOMother
import com.germandebustamante.ringtonemanager.core.model.error.ErrorBOMother
import com.germandebustamante.ringtonemanager.domain.authorization.repository.AuthenticationRepository
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class GetUserFlowUseCaseTest {

    private lateinit var sut: GetUserFlowUseCase

    @MockK
    private lateinit var authenticationRepository: AuthenticationRepository

    @BeforeEach
    fun setUp() {
        sut = GetUserFlowUseCase(authenticationRepository)
    }

    @Test
    fun `GIVEN authenticated user WHEN invoked THEN emits user`() = runTest {
        // Given
        val user = UserBOMother.default()
        every { authenticationRepository.currentUser } returns flowOf(user.right())

        // When & Then
        sut().test {
            assertEquals(user, awaitItem().getOrNull())
            awaitComplete()
        }
    }

    @Test
    fun `GIVEN no authenticated user WHEN invoked THEN emits null`() = runTest {
        // Given
        every { authenticationRepository.currentUser } returns flowOf(null.right())

        // When & Then
        sut().test {
            assertNull(awaitItem().getOrNull())
            awaitComplete()
        }
    }

    @Test
    fun `GIVEN error WHEN invoked THEN emits error`() = runTest {
        // Given
        val error = ErrorBOMother.unknown()
        every { authenticationRepository.currentUser } returns flowOf(error.left())

        // When & Then
        sut().test {
            assertTrue(awaitItem().isLeft())
            awaitComplete()
        }
    }
}
