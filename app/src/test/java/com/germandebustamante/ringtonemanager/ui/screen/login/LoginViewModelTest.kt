package com.germandebustamante.ringtonemanager.ui.screen.login

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import com.germandebustamante.ringtonemanager.core.model.authorization.LoginTypeBO
import com.germandebustamante.ringtonemanager.core.model.authorization.UserBOMother
import com.germandebustamante.ringtonemanager.core.model.error.ErrorBOMother
import com.germandebustamante.ringtonemanager.core.navigation.action.Navigator
import com.germandebustamante.ringtonemanager.core.navigation.destination.Destination
import com.germandebustamante.ringtonemanager.domain.authorization.usecase.GetUserFlowUseCase
import com.germandebustamante.ringtonemanager.domain.authorization.usecase.SignInUserUseCase
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
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MockKExtension::class)
class LoginViewModelTest {

    private lateinit var sut: LoginViewModel

    @MockK
    private lateinit var signInUserUseCase: SignInUserUseCase

    @MockK
    private lateinit var currentUserFlowUseCase: GetUserFlowUseCase

    @MockK(relaxed = true)
    private lateinit var navigator: Navigator

    private val testDispatcher = UnconfinedTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Nested
    inner class Init {

        @Test
        fun `GIVEN user is already logged in WHEN init THEN navigates up`() = runTest {
            // Given
            val user = UserBOMother.default()
            every { currentUserFlowUseCase() } returns flowOf(user.right())

            // When
            buildSut()

            // Then
            coVerify(exactly = 1) { navigator.navigateUp() }
        }

        @Test
        fun `GIVEN user is not logged in WHEN init THEN stays on screen`() = runTest {
            // Given
            every { currentUserFlowUseCase() } returns flowOf(null.right())

            // When
            buildSut()

            // Then
            coVerify(exactly = 0) { navigator.navigateUp() }
        }
    }

    @Nested
    inner class SignIn {

        @BeforeEach
        fun setup() {
            every { currentUserFlowUseCase() } returns flowOf(null.right())
            buildSut()
        }

        @Test
        fun `GIVEN valid credentials WHEN signIn succeeds THEN loading is false`() = runTest {
            // Given
            sut.updateEmail("test@example.com")
            sut.updatePassword("Password1!")
            coEvery { signInUserUseCase(any()) } returns Unit.right()

            // When
            sut.onSignInButtonClicked()

            // Then
            sut.state.test {
                val state = awaitItem()
                assertFalse(state.loading)
                assertNull(state.error)
            }
        }

        @Test
        fun `GIVEN valid credentials WHEN signIn fails with server error THEN error is set`() = runTest {
            // Given
            val error = ErrorBOMother.serverError()
            sut.updateEmail("test@example.com")
            sut.updatePassword("Password1!")
            coEvery { signInUserUseCase(any()) } returns error.left()

            // When
            sut.onSignInButtonClicked()

            // Then
            sut.state.test {
                val state = awaitItem()
                assertEquals(error, state.error)
                assertFalse(state.loading)
            }
        }

        @Test
        fun `GIVEN invalid email WHEN signIn button clicked THEN email validation error shown`() = runTest {
            // Given
            sut.updateEmail("not-an-email")
            sut.updatePassword("Password1!")

            // When
            sut.onSignInButtonClicked()

            // Then
            sut.state.test {
                val state = awaitItem()
                assertFalse(state.email.isValid)
            }
        }

        @Test
        fun `GIVEN invalid inputs WHEN signIn button clicked THEN use case is not called`() = runTest {
            // Given
            sut.updateEmail("not-an-email")
            sut.updatePassword("")

            // When
            sut.onSignInButtonClicked()

            // Then
            coVerify(exactly = 0) { signInUserUseCase(any()) }
        }
    }

    @Nested
    inner class GoogleSignIn {

        @BeforeEach
        fun setup() {
            every { currentUserFlowUseCase() } returns flowOf(null.right())
            buildSut()
        }

        @Test
        fun `GIVEN google token WHEN signIn succeeds THEN loading is false`() = runTest {
            // Given
            coEvery { signInUserUseCase(LoginTypeBO.Google("token_123")) } returns Unit.right()

            // When
            sut.onGoogleIdTokenReceived("token_123")

            // Then
            sut.state.test {
                val state = awaitItem()
                assertFalse(state.loading)
                assertNull(state.error)
            }
        }

        @Test
        fun `GIVEN google token WHEN signIn fails THEN error is set`() = runTest {
            // Given
            val error = ErrorBOMother.invalidCredentials()
            coEvery { signInUserUseCase(any()) } returns error.left()

            // When
            sut.onGoogleIdTokenReceived("token_123")

            // Then
            sut.state.test {
                val state = awaitItem()
                assertNotNull(state.error)
            }
        }
    }

    @Test
    fun `GIVEN error in state WHEN cleanError THEN error is null`() = runTest {
        // Given
        every { currentUserFlowUseCase() } returns flowOf(null.right())
        buildSut()
        coEvery { signInUserUseCase(any()) } returns ErrorBOMother.serverError().left()
        sut.updateEmail("test@example.com")
        sut.updatePassword("Password1!")
        sut.onSignInButtonClicked()

        // When
        sut.cleanError()

        // Then
        assertNull(sut.state.value.error)
    }

    @Test
    fun `GIVEN user on login screen WHEN forgot password clicked THEN navigates to ForgotPasswordScreen`() = runTest {
        // Given
        every { currentUserFlowUseCase() } returns flowOf(null.right())
        buildSut()

        // When
        sut.onPasswordForgottenClicked()

        // Then
        coVerify(exactly = 1) { navigator.navigate(Destination.ForgotPasswordScreen) }
    }

    @Test
    fun `GIVEN user on login screen WHEN create account clicked THEN navigates to RegisterScreen`() = runTest {
        // Given
        every { currentUserFlowUseCase() } returns flowOf(null.right())
        buildSut()

        // When
        sut.onCreateNewAccountClicked()

        // Then
        coVerify(exactly = 1) { navigator.navigate(Destination.RegisterScreen) }
    }

    private fun buildSut() {
        sut = LoginViewModel(
            signInUserUseCase = signInUserUseCase,
            currentUserFlowUseCase = currentUserFlowUseCase,
            navigator = navigator,
        )
        sut.start()
    }
}
