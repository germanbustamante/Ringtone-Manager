package com.germandebustamante.ringtonemanager.core.navigation.action

import app.cash.turbine.test
import com.germandebustamante.ringtonemanager.core.navigation.destination.Destination
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DefaultNavigatorTest {

    private lateinit var sut: DefaultNavigator

    @BeforeEach
    fun setUp() {
        sut = DefaultNavigator()
    }

    @Test
    fun `GIVEN navigator WHEN navigate to destination THEN emits Navigate action`() = runTest {
        // Given
        val destination = Destination.HomeScreen

        // When & Then
        sut.navigationActions.test {
            sut.navigate(destination)

            val action = awaitItem()
            assertTrue(action is NavigationAction.Navigate)
            assertEquals(destination, (action as NavigationAction.Navigate).destination)
        }
    }

    @Test
    fun `GIVEN navigator WHEN navigate to ringtone detail THEN emits Navigate action with ringtone id`() = runTest {
        // Given
        val ringtoneId = "ringtone-123"
        val destination = Destination.RingtoneDetailScreen(ringtoneId)

        // When & Then
        sut.navigationActions.test {
            sut.navigate(destination)

            val action = awaitItem()
            assertTrue(action is NavigationAction.Navigate)
            assertEquals(destination, (action as NavigationAction.Navigate).destination)
            assertEquals(ringtoneId, destination.ringtoneId)
        }
    }

    @Test
    fun `GIVEN navigator WHEN navigateUp THEN emits Back action`() = runTest {
        // When & Then
        sut.navigationActions.test {
            sut.navigateUp()

            val action = awaitItem()
            assertTrue(action is NavigationAction.Back)
        }
    }

    @Test
    fun `GIVEN navigator WHEN multiple navigation actions THEN emits all actions in order`() = runTest {
        // Given
        val destination1 = Destination.HomeScreen
        val destination2 = Destination.SettingsScreen

        // When & Then
        sut.navigationActions.test {
            sut.navigate(destination1)
            val action1 = awaitItem()
            assertTrue(action1 is NavigationAction.Navigate)
            assertEquals(destination1, (action1 as NavigationAction.Navigate).destination)

            sut.navigate(destination2)
            val action2 = awaitItem()
            assertTrue(action2 is NavigationAction.Navigate)
            assertEquals(destination2, (action2 as NavigationAction.Navigate).destination)

            sut.navigateUp()
            val action3 = awaitItem()
            assertTrue(action3 is NavigationAction.Back)
        }
    }

    @Test
    fun `GIVEN navigator WHEN navigate to login screen THEN emits Navigate action`() = runTest {
        // Given
        val destination = Destination.LoginScreen

        // When & Then
        sut.navigationActions.test {
            sut.navigate(destination)

            val action = awaitItem()
            assertTrue(action is NavigationAction.Navigate)
            assertEquals(destination, (action as NavigationAction.Navigate).destination)
        }
    }

    @Test
    fun `GIVEN navigator WHEN navigate to register screen THEN emits Navigate action`() = runTest {
        // Given
        val destination = Destination.RegisterScreen

        // When & Then
        sut.navigationActions.test {
            sut.navigate(destination)

            val action = awaitItem()
            assertTrue(action is NavigationAction.Navigate)
            assertEquals(destination, (action as NavigationAction.Navigate).destination)
        }
    }

    @Test
    fun `GIVEN navigator WHEN navigate to forgot password screen THEN emits Navigate action`() = runTest {
        // Given
        val destination = Destination.ForgotPasswordScreen

        // When & Then
        sut.navigationActions.test {
            sut.navigate(destination)

            val action = awaitItem()
            assertTrue(action is NavigationAction.Navigate)
            assertEquals(destination, (action as NavigationAction.Navigate).destination)
        }
    }
}
