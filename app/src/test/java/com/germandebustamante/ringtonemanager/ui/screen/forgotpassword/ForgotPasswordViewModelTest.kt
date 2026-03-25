package com.germandebustamante.ringtonemanager.ui.screen.forgotpassword

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import com.germandebustamante.ringtonemanager.core.model.error.ErrorBOMother
import com.germandebustamante.ringtonemanager.core.navigation.action.Navigator
import com.germandebustamante.ringtonemanager.domain.authorization.usecase.ForgotPasswordUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
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
class ForgotPasswordViewModelTest {

    private lateinit var sut: ForgotPasswordViewModel

    @MockK
    private lateinit var forgotPasswordUseCase: ForgotPasswordUseCase

    @MockK(relaxed = true)
    private lateinit var navigator: Navigator

    private val testDispatcher = UnconfinedTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        sut = ForgotPasswordViewModel(navigator, forgotPasswordUseCase)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `GIVEN initial state WHEN created THEN email is empty`() {
        assertTrue(sut.state.value.email.value.isEmpty())
    }

    @Test
    fun `GIVEN new email WHEN updateEmail called THEN state is updated`() {
        // When
        sut.updateEmail("test@example.com")

        // Then
        assertEquals("test@example.com", sut.state.value.email.value)
    }

    @Test
    fun `GIVEN error in state WHEN cleanError THEN error is null`() = runTest {
        // Given
        coEvery { forgotPasswordUseCase(any()) } returns ErrorBOMother.serverError().left()
        sut.updateEmail("test@example.com")
        sut.onRestorePasswordClicked()

        // When
        sut.cleanError()

        // Then
        assertNull(sut.state.value.error)
    }

    @Test
    fun `GIVEN valid email WHEN restoring password succeeds THEN emailSentEvent fires`() = runTest {
        // Given
        coEvery { forgotPasswordUseCase("test@example.com") } returns Unit.right()
        sut.updateEmail("test@example.com")

        // When & Then
        sut.emailSentEvent.test {
            sut.onRestorePasswordClicked()
            awaitItem()
        }
    }

    @Test
    fun `GIVEN valid email WHEN restoring password succeeds THEN loading is false`() = runTest {
        // Given
        coEvery { forgotPasswordUseCase(any()) } returns Unit.right()
        sut.updateEmail("test@example.com")

        // When
        sut.onRestorePasswordClicked()

        // Then
        assertFalse(sut.state.value.loading)
    }

    @Test
    fun `GIVEN server error WHEN restoring password THEN error is set`() = runTest {
        // Given
        val error = ErrorBOMother.serverError()
        coEvery { forgotPasswordUseCase(any()) } returns error.left()
        sut.updateEmail("test@example.com")

        // When
        sut.onRestorePasswordClicked()

        // Then
        assertNotNull(sut.state.value.error)
        assertEquals(error, sut.state.value.error)
    }

    @Test
    fun `GIVEN invalid email WHEN restoring password THEN shows email validation error`() = runTest {
        // Given
        sut.updateEmail("invalid-email")

        // When
        sut.onRestorePasswordClicked()

        // Then
        assertFalse(sut.state.value.email.isValid)
        coVerify(exactly = 0) { forgotPasswordUseCase(any()) }
    }
}
