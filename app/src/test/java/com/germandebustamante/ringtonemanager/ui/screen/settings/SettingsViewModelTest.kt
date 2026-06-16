package com.germandebustamante.ringtonemanager.ui.screen.settings

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import com.germandebustamante.ringtonemanager.core.model.authorization.UserBOMother
import com.germandebustamante.ringtonemanager.core.model.error.ErrorBOMother
import com.germandebustamante.ringtonemanager.core.navigation.action.Navigator
import com.germandebustamante.ringtonemanager.core.navigation.destination.Destination
import com.germandebustamante.ringtonemanager.domain.authorization.usecase.GetUserFlowUseCase
import com.germandebustamante.ringtonemanager.domain.authorization.usecase.SignOutUserUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.Runs
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
class SettingsViewModelTest {

    private lateinit var sut: SettingsViewModel

    @MockK
    private lateinit var getUserFlowUseCase: GetUserFlowUseCase

    @MockK
    private lateinit var signOutUserUseCase: SignOutUserUseCase

    @MockK(relaxed = true)
    private lateinit var navigator: Navigator

    @MockK(relaxed = true)
    private lateinit var changePasswordUseCase: com.germandebustamante.ringtonemanager.domain.authorization.usecase.ChangePasswordUseCase

    private val testDispatcher = UnconfinedTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `GIVEN user is logged in WHEN init THEN userLogged is true`() = runTest {
        // Given
        val user = UserBOMother.default()
        every { getUserFlowUseCase() } returns flowOf(user.right())

        // When
        buildSut()

        // Then
        sut.state.test {
            val state = awaitItem()
            assertTrue(state.userLogged)
            assertFalse(state.isLoading)
        }
    }

    @Test
    fun `GIVEN user is not logged in WHEN init THEN userLogged is false`() = runTest {
        // Given
        every { getUserFlowUseCase() } returns flowOf(null.right())

        // When
        buildSut()

        // Then
        sut.state.test {
            val state = awaitItem()
            assertFalse(state.userLogged)
            assertFalse(state.isLoading)
        }
    }

    @Test
    fun `GIVEN user flow error WHEN init THEN error state is set`() = runTest {
        // Given
        val error = ErrorBOMother.serverError()
        every { getUserFlowUseCase() } returns flowOf(error.left())

        // When
        buildSut()

        // Then
        sut.state.test {
            val state = awaitItem()
            assertEquals(error, state.error)
            assertFalse(state.isLoading)
        }
    }

    @Test
    fun `GIVEN user is logged in WHEN signOut called THEN delegates to use case`() = runTest {
        // Given
        every { getUserFlowUseCase() } returns flowOf(UserBOMother.default().right())
        coEvery { signOutUserUseCase() } just Runs
        buildSut()

        // When
        sut.signOut()

        // Then
        coVerify(exactly = 1) { signOutUserUseCase() }
    }

    @Test
    fun `GIVEN user not logged in WHEN navigateToSignIn called THEN navigates to LoginScreen`() = runTest {
        // Given
        every { getUserFlowUseCase() } returns flowOf(null.right())
        buildSut()

        // When
        sut.navigateToSignIn()

        // Then
        coVerify(exactly = 1) { navigator.navigate(Destination.LoginScreen) }
    }

    @Test
    fun `GIVEN user not logged in WHEN navigateToSignUp called THEN navigates to RegisterScreen`() = runTest {
        // Given
        every { getUserFlowUseCase() } returns flowOf(null.right())
        buildSut()

        // When
        sut.navigateToSignUp()

        // Then
        coVerify(exactly = 1) { navigator.navigate(Destination.RegisterScreen) }
    }

    @Test
    fun `GIVEN error in state WHEN cleanError THEN error is null`() = runTest {
        // Given
        val error = ErrorBOMother.serverError()
        every { getUserFlowUseCase() } returns flowOf(error.left())
        buildSut()

        // When
        sut.cleanError()

        // Then
        assertNull(sut.state.value.error)
    }

    private fun buildSut() {
        sut = SettingsViewModel(getUserFlowUseCase, signOutUserUseCase, changePasswordUseCase, navigator)
    }
}
