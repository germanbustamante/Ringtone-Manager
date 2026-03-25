package com.germandebustamante.ringtonemanager.ui.screen.register

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import com.germandebustamante.ringtonemanager.core.model.authorization.UserBOMother
import com.germandebustamante.ringtonemanager.core.model.error.ErrorBOMother
import com.germandebustamante.ringtonemanager.core.navigation.action.Navigator
import com.germandebustamante.ringtonemanager.domain.authorization.usecase.GetUserFlowUseCase
import com.germandebustamante.ringtonemanager.domain.authorization.usecase.SignInUserUseCase
import com.germandebustamante.ringtonemanager.domain.authorization.usecase.SignUpUserUseCase
import io.mockk.coEvery
import io.mockk.coVerify
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
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MockKExtension::class)
class RegisterViewModelTest {

    private lateinit var sut: RegisterViewModel

    @MockK
    private lateinit var signUpUserUseCase: SignUpUserUseCase

    @MockK
    private lateinit var currentUserFlowUseCase: GetUserFlowUseCase

    @MockK
    private lateinit var signInUserUseCase: SignInUserUseCase

    @MockK(relaxed = true)
    private lateinit var navigator: Navigator

    private val testDispatcher = UnconfinedTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { currentUserFlowUseCase() } returns flowOf(null.right())
        buildSut()
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `GIVEN user already logged in WHEN init THEN navigates up`() = runTest {
        // Given
        every { currentUserFlowUseCase() } returns flowOf(UserBOMother.default().right())

        // When
        buildSut()

        // Then
        coVerify(exactly = 1) { navigator.navigateUp() }
    }

    @Test
    fun `GIVEN valid inputs WHEN signUp succeeds THEN loading is false and no error`() = runTest {
        // Given
        givenValidInputs()
        coEvery { signUpUserUseCase(any(), any(), any()) } returns Unit.right()

        // When
        sut.onSignUpButtonClicked()

        // Then
        sut.state.test {
            val state = awaitItem()
            assertFalse(state.loading)
            assertNull(state.error)
        }
    }

    @Test
    fun `GIVEN valid inputs WHEN signUp fails THEN error is set`() = runTest {
        // Given
        val error = ErrorBOMother.emailAddressAlreadyInUse()
        givenValidInputs()
        coEvery { signUpUserUseCase(any(), any(), any()) } returns error.left()

        // When
        sut.onSignUpButtonClicked()

        // Then
        sut.state.test {
            val state = awaitItem()
            assertEquals(error, state.error)
            assertFalse(state.loading)
        }
    }

    @Test
    fun `GIVEN invalid email WHEN signUp clicked THEN shows email validation error`() = runTest {
        // Given
        sut.updateEmail("invalid")
        sut.updateName("John Doe")
        sut.updatePassword("Password1!")
        sut.updateRepeatPassword("Password1!")

        // When
        sut.onSignUpButtonClicked()

        // Then
        sut.state.test {
            val state = awaitItem()
            assertFalse(state.email.isValid)
        }
        coVerify(exactly = 0) { signUpUserUseCase(any(), any(), any()) }
    }

    @Test
    fun `GIVEN passwords dont match WHEN signUp clicked THEN shows repeat password error`() = runTest {
        // Given
        sut.updateEmail("test@example.com")
        sut.updateName("John Doe")
        sut.updatePassword("Password1!")
        sut.updateRepeatPassword("Different1!")

        // When
        sut.onSignUpButtonClicked()

        // Then
        sut.state.test {
            val state = awaitItem()
            assertFalse(state.repeatPassword.isValid)
        }
    }

    @Test
    fun `GIVEN error in state WHEN cleanError THEN error is null`() = runTest {
        // Given
        givenValidInputs()
        coEvery { signUpUserUseCase(any(), any(), any()) } returns ErrorBOMother.serverError().left()
        sut.onSignUpButtonClicked()

        // When
        sut.cleanError()

        // Then
        assertNull(sut.state.value.error)
    }

    private fun givenValidInputs() {
        sut.updateEmail("test@example.com")
        sut.updateName("John Doe")
        sut.updatePassword("Password1!")
        sut.updateRepeatPassword("Password1!")
    }

    private fun buildSut() {
        sut = RegisterViewModel(signUpUserUseCase, currentUserFlowUseCase, signInUserUseCase, navigator)
    }
}
