package com.germandebustamante.ringtonemanager.data.remote.manager

import com.germandebustamante.ringtonemanager.core.model.error.ErrorBO
import com.germandebustamante.ringtonemanager.data.remote.manager.FirestoreManager.toError
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.storage.StorageException
import io.mockk.every
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class FirestoreManagerErrorMappingTest {

    @Test
    fun `GIVEN StorageException WHEN mapping to error THEN returns Server error`() {
        // Given
        val storageException: StorageException = io.mockk.mockk(relaxed = true) {
            every { errorCode } returns 500
            every { message } returns "Storage error"
        }

        // When
        val result = storageException.toError()

        // Then
        assertTrue(result is ErrorBO.Server)
        assertEquals(500, (result as ErrorBO.Server).code)
    }

    @Test
    fun `GIVEN RuntimeException WHEN mapping to error THEN returns ParcelizeException`() {
        // Given
        val runtimeException = RuntimeException("Runtime error")

        // When
        val result = runtimeException.toError()

        // Then
        assertTrue(result is ErrorBO.ParcelizeException)
    }

    @Test
    fun `GIVEN FirebaseAuthUserCollisionException WHEN mapping to error THEN returns EmailAddressAlreadyInUse`() {
        // Given
        val exception: FirebaseAuthUserCollisionException = io.mockk.mockk(relaxed = true)

        // When
        val result = exception.toError()

        // Then
        assertTrue(result is ErrorBO.EmailAddressAlreadyInUse)
    }

    @Test
    fun `GIVEN FirebaseAuthInvalidCredentialsException WHEN mapping to error THEN returns InvalidCredentials`() {
        // Given
        val exception: FirebaseAuthInvalidCredentialsException = io.mockk.mockk(relaxed = true)

        // When
        val result = exception.toError()

        // Then
        assertTrue(result is ErrorBO.InvalidCredentials)
    }

    @Test
    fun `GIVEN generic Exception WHEN mapping to error THEN returns Unknown error`() {
        // Given
        val exception = Exception("Generic error")

        // When
        val result = exception.toError()

        // Then
        assertTrue(result is ErrorBO.Unknown)
        assertEquals("Generic error", (result as ErrorBO.Unknown).message)
    }

    @Test
    fun `GIVEN Exception with null message WHEN mapping to error THEN returns Unknown error with null message`() {
        // Given
        val exception = Exception(null as String?)

        // When
        val result = exception.toError()

        // Then
        assertTrue(result is ErrorBO.Unknown)
        assertEquals(null, (result as ErrorBO.Unknown).message)
    }

    @Test
    fun `GIVEN IllegalArgumentException WHEN mapping to error THEN returns ParcelizeException`() {
        // Given (IllegalArgumentException is a RuntimeException subclass)
        val exception = IllegalArgumentException("Invalid argument")

        // When
        val result = exception.toError()

        // Then
        assertTrue(result is ErrorBO.ParcelizeException)
    }

    @Test
    fun `GIVEN NullPointerException WHEN mapping to error THEN returns ParcelizeException`() {
        // Given (NullPointerException is a RuntimeException subclass)
        val exception = NullPointerException("Null pointer")

        // When
        val result = exception.toError()

        // Then
        assertTrue(result is ErrorBO.ParcelizeException)
    }

    @Test
    fun `GIVEN StorageException with code 404 WHEN mapping to error THEN returns Server error with 404`() {
        // Given
        val storageException: StorageException = io.mockk.mockk(relaxed = true) {
            every { errorCode } returns 404
        }

        // When
        val result = storageException.toError()

        // Then
        assertTrue(result is ErrorBO.Server)
        assertEquals(404, (result as ErrorBO.Server).code)
    }

    @Test
    fun `GIVEN StorageException with code 403 WHEN mapping to error THEN returns Server error with 403`() {
        // Given
        val storageException: StorageException = io.mockk.mockk(relaxed = true) {
            every { errorCode } returns 403
            every { message } returns "Forbidden"
        }

        // When
        val result = storageException.toError()

        // Then
        assertTrue(result is ErrorBO.Server)
        assertEquals(403, (result as ErrorBO.Server).code)
    }
}
