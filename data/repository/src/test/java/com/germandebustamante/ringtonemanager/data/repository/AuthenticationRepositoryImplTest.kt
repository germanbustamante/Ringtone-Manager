package com.germandebustamante.ringtonemanager.data.repository

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import com.germandebustamante.ringtonemanager.core.model.authorization.LoginTypeBO
import com.germandebustamante.ringtonemanager.core.model.authorization.UserBOMother
import com.germandebustamante.ringtonemanager.core.model.di.TestDispatcherProvider
import com.germandebustamante.ringtonemanager.core.model.error.ErrorBOMother
import com.germandebustamante.ringtonemanager.data.datasource.AuthenticationRemoteDataSource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.Runs
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class AuthenticationRepositoryImplTest {

    private lateinit var sut: AuthenticationRepositoryImpl

    @MockK(relaxed = true)
    private lateinit var remoteDataSource: AuthenticationRemoteDataSource

    @BeforeEach
    fun setUp() {
        sut = AuthenticationRepositoryImpl(remoteDataSource, TestDispatcherProvider())
    }

    @Test
    fun `GIVEN email and password WHEN signIn with Default type THEN delegates to remote data source`() = runTest {
        // Given
        val email = "test@example.com"
        val password = "password123"
        coEvery { remoteDataSource.signIn(email, password) } returns Unit.right()

        // When
        val result = sut.signIn(LoginTypeBO.Default(email, password))

        // Then
        assertTrue(result.isRight())
        coVerify(exactly = 1) { remoteDataSource.signIn(email, password) }
    }

    @Test
    fun `GIVEN server error WHEN signIn with Default type THEN returns error`() = runTest {
        // Given
        val error = ErrorBOMother.serverError()
        coEvery { remoteDataSource.signIn(any(), any()) } returns error.left()

        // When
        val result = sut.signIn(LoginTypeBO.Default("test@example.com", "password123"))

        // Then
        assertTrue(result.isLeft())
        assertEquals(error, result.leftOrNull())
    }

    @Test
    fun `GIVEN google token WHEN signIn with Google type THEN delegates to remote data source`() = runTest {
        // Given
        val token = "google_token_123"
        coEvery { remoteDataSource.googleSignIn(token) } returns Unit.right()

        // When
        val result = sut.signIn(LoginTypeBO.Google(token))

        // Then
        assertTrue(result.isRight())
        coVerify(exactly = 1) { remoteDataSource.googleSignIn(token) }
    }

    @Test
    fun `GIVEN valid data WHEN signUp THEN delegates to remote data source`() = runTest {
        // Given
        val email = "test@example.com"
        val password = "password123"
        val name = "Test User"
        coEvery { remoteDataSource.signUp(email, password, name) } returns Unit.right()

        // When
        val result = sut.signUp(email, password, name)

        // Then
        assertTrue(result.isRight())
        coVerify(exactly = 1) { remoteDataSource.signUp(email, password, name) }
    }

    @Test
    fun `GIVEN valid email WHEN forgotPassword THEN delegates to remote data source`() = runTest {
        // Given
        val email = "test@example.com"
        coEvery { remoteDataSource.forgotPassword(email) } returns Unit.right()

        // When
        val result = sut.forgotPassword(email)

        // Then
        assertTrue(result.isRight())
        coVerify(exactly = 1) { remoteDataSource.forgotPassword(email) }
    }

    @Test
    fun `GIVEN signOut called WHEN signOut THEN delegates to remote data source`() = runTest {
        // Given
        every { remoteDataSource.signOut() } just Runs

        // When
        sut.signOut()

        // Then
        coVerify(exactly = 1) { remoteDataSource.signOut() }
    }

    @Test
    fun `GIVEN user is logged in WHEN collecting currentUser THEN emits user`() = runTest {
        // Given - sut must be built after stub since currentUser is initialized at construction
        val user = UserBOMother.default()
        every { remoteDataSource.currentUser } returns flowOf(user.right())
        val localSut = AuthenticationRepositoryImpl(remoteDataSource, TestDispatcherProvider())

        // When & Then
        localSut.currentUser.test {
            val item = awaitItem()
            assertTrue(item.isRight())
            assertEquals(user, item.getOrNull())
            awaitComplete()
        }
    }

    @Test
    fun `GIVEN user is not logged in WHEN collecting currentUser THEN emits null`() = runTest {
        // Given - sut must be built after stub since currentUser is initialized at construction
        every { remoteDataSource.currentUser } returns flowOf(null.right())
        val localSut = AuthenticationRepositoryImpl(remoteDataSource, TestDispatcherProvider())

        // When & Then
        localSut.currentUser.test {
            val item = awaitItem()
            assertTrue(item.isRight())
            assertTrue(item.getOrNull() == null)
            awaitComplete()
        }
    }
}
