package com.germandebustamante.ringtonemanager.core.navigation.action

import androidx.compose.runtime.mutableStateOf
import androidx.navigation3.runtime.NavBackStack
import com.germandebustamante.ringtonemanager.core.navigation.destination.Destination
import com.germandebustamante.ringtonemanager.core.navigation.destination.TabDestination
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class NavigationStateHolderTest {

    private lateinit var sut: NavigationStateHolder

    @MockK
    private lateinit var homeBackStack: NavBackStack<Destination>

    @MockK
    private lateinit var settingsBackStack: NavBackStack<Destination>

    private lateinit var backStacks: Map<TabDestination, NavBackStack<Destination>>

    @BeforeEach
    fun setUp() {
        backStacks = mapOf(
            TabDestination.Home to homeBackStack,
            TabDestination.Settings to settingsBackStack
        )
    }

    @Nested
    inner class Init {
        @Test
        fun `GIVEN navigation state holder WHEN initialized THEN starts with home tab`() {
            // Given & When
            givenNavigationStateHolderWithHomeTab()

            // Then
            assertEquals(TabDestination.Home, sut.currentTab)
            assertEquals(TabDestination.Home, sut.startTab)
        }
    }

    @Nested
    inner class HomeTab {
        @Test
        fun `GIVEN home tab selected WHEN navigate to ringtone detail THEN adds to home back stack`() {
            // Given
            givenNavigationStateHolderWithHomeTab()
            givenHomeBackStackAddSuccess()
            val destination = Destination.RingtoneDetailScreen("ringtone-123")

            // When
            sut.navigate(destination)

            // Then
            verify(exactly = 1) { homeBackStack.add(destination) }
            verify(exactly = 0) { settingsBackStack.add(any()) }
        }

        @Test
        fun `GIVEN home tab selected WHEN navigate to settings tab THEN switches to settings tab`() {
            // Given
            givenNavigationStateHolderWithHomeTab()

            // When
            sut.navigate(Destination.SettingsScreen)

            // Then
            assertEquals(TabDestination.Settings, sut.currentTab)
        }
    }

    @Nested
    inner class SettingsTab {
        @Test
        fun `GIVEN settings tab selected WHEN navigate to home tab THEN switches to home tab`() {
            // Given
            givenNavigationStateHolderWithSettingsTab()

            // When
            sut.navigate(Destination.HomeScreen)

            // Then
            assertEquals(TabDestination.Home, sut.currentTab)
        }


    }

    @Nested
    inner class OnTabSelected {
        @Test
        fun `GIVEN home tab WHEN onTabSelected with settings THEN switches to settings tab`() {
            // Given
            givenNavigationStateHolderWithHomeTab()

            // When
            sut.onTabSelected(TabDestination.Settings)

            // Then
            assertEquals(TabDestination.Settings, sut.currentTab)
        }

        @Test
        fun `GIVEN settings tab WHEN onTabSelected with home THEN switches to home tab`() {
            // Given
            givenNavigationStateHolderWithSettingsTab()

            // When
            sut.onTabSelected(TabDestination.Home)

            // Then
            assertEquals(TabDestination.Home, sut.currentTab)
        }
    }

    @Nested
    inner class NavigateToAuthScreens {
        @Test
        fun `GIVEN home tab WHEN navigate to login screen THEN adds to settings back stack`() {
            // Given
            givenNavigationStateHolderWithSettingsTab()
            givenSettingsBackStackAddSuccess()

            val destination = Destination.LoginScreen

            // When
            sut.navigate(destination)

            // Then
            verify(exactly = 1) { settingsBackStack.add(destination) }
        }

        @Test
        fun `GIVEN home tab WHEN navigate to register screen THEN adds to settings back stack`() {
            // Given
            givenNavigationStateHolderWithSettingsTab()
            givenSettingsBackStackAddSuccess()

            val destination = Destination.RegisterScreen

            // When
            sut.navigate(destination)

            // Then
            verify(exactly = 1) { settingsBackStack.add(destination) }
        }

        @Test
        fun `GIVEN home tab WHEN navigate to forgot password screen THEN adds to settings stack`() {
            // Given
            givenNavigationStateHolderWithSettingsTab()
            givenSettingsBackStackAddSuccess()

            val destination = Destination.ForgotPasswordScreen

            // When
            sut.navigate(destination)

            // Then
            verify(exactly = 1) { settingsBackStack.add(destination) }
        }
    }


    //region Stubs
    private fun givenNavigationStateHolderWithHomeTab() {
        sut = NavigationStateHolder(
            startTab = TabDestination.Home,
            selectedTab = mutableStateOf(TabDestination.Home),
            backStacks = backStacks
        )
    }

    private fun givenNavigationStateHolderWithSettingsTab() {
        sut = NavigationStateHolder(
            startTab = TabDestination.Home,
            selectedTab = mutableStateOf(TabDestination.Settings),
            backStacks = backStacks
        )
    }

    private fun givenHomeBackStackAddSuccess() {
        every { homeBackStack.isEmpty() } returns false
        every { homeBackStack.add(any()) } returns true
    }

    private fun givenSettingsBackStackAddSuccess() {
        every { settingsBackStack.isEmpty() } returns false
        every { settingsBackStack.add(any()) } returns true
    }
    //endregion
}
