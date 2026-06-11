package com.germandebustamante.ringtonemanager.data.remote.manager

import com.germandebustamante.ringtonemanager.core.model.error.ErrorBO
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import io.mockk.coEvery
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class FirebaseAuthManagerTest {

    private lateinit var sut: FirebaseAuthManager

    @BeforeEach
    fun setUp() {
        sut = FirebaseAuthManager()
        mockkStatic(AWAIT_FACADE)
    }

    @AfterEach
    fun tearDown() {
        unmockkStatic(AWAIT_FACADE)
    }

    @Test
    fun `GIVEN task succeeds WHEN execute THEN returns Right with result`() = runTest {
        // Given
        val authResult = mockk<AuthResult>()
        val task = mockk<Task<AuthResult>>()
        coEvery { task.await() } returns authResult

        // When
        val result = sut.execute { task }

        // Then
        assertTrue(result.isRight())
        assertEquals(authResult, result.getOrNull())
    }

    @Test
    fun `GIVEN task throws invalid credentials WHEN execute THEN returns Left with InvalidCredentials`() = runTest {
        // Given
        val task = mockk<Task<AuthResult>>()
        coEvery { task.await() } throws mockk<FirebaseAuthInvalidCredentialsException>(relaxed = true)

        // When
        val result = sut.execute { task }

        // Then
        assertTrue(result.isLeft())
        assertTrue(result.leftOrNull() is ErrorBO.InvalidCredentials)
    }

    @Test
    fun `GIVEN task throws generic exception WHEN execute THEN returns Left with Unknown`() = runTest {
        // Given
        val task = mockk<Task<AuthResult>>()
        coEvery { task.await() } throws RuntimeException("boom")

        // When
        val result = sut.execute { task }

        // Then
        assertTrue(result.isLeft())
        assertTrue(result.leftOrNull() is ErrorBO.Unknown)
    }

    @Test
    fun `GIVEN task succeeds WHEN executeVoid THEN returns Right Unit`() = runTest {
        // Given
        val task = mockk<Task<Void>>()
        coEvery { task.await() } returns mockk()

        // When
        val result = sut.executeVoid { task }

        // Then
        assertTrue(result.isRight())
    }

    @Test
    fun `GIVEN task throws WHEN executeVoid THEN returns Left with Unknown`() = runTest {
        // Given
        val task = mockk<Task<Void>>()
        coEvery { task.await() } throws RuntimeException("boom")

        // When
        val result = sut.executeVoid { task }

        // Then
        assertTrue(result.isLeft())
        assertTrue(result.leftOrNull() is ErrorBO.Unknown)
    }

    companion object {
        private const val AWAIT_FACADE = "kotlinx.coroutines.tasks.TasksKt"
    }
}
