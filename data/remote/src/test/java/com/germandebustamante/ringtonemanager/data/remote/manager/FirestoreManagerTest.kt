package com.germandebustamante.ringtonemanager.data.remote.manager

import com.germandebustamante.ringtonemanager.core.model.error.ErrorBO
import com.germandebustamante.ringtonemanager.core.model.ringtone.RingtoneBO
import com.germandebustamante.ringtonemanager.data.remote.model.ringtone.RingtoneDTO
import com.germandebustamante.ringtonemanager.data.remote.model.ringtone.toDomain
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import io.mockk.coEvery
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class FirestoreManagerTest {

    private lateinit var sut: FirestoreManager

    @BeforeEach
    fun setUp() {
        sut = FirestoreManager()
        mockkStatic(AWAIT_FACADE)
    }

    @AfterEach
    fun tearDown() {
        unmockkStatic(AWAIT_FACADE)
    }

    @Test
    fun `GIVEN document exists WHEN getDocument THEN returns Right with mapped value`() = runTest {
        // Given
        val dto = RingtoneDTO(id = "1", name = "Ringtone", artist = "Artist", file_url = "url", popularity = 3)
        val snapshot = mockk<DocumentSnapshot>()
        val task = mockk<Task<DocumentSnapshot>>()
        coEvery { task.await() } returns snapshot
        every { snapshot.toObject(RingtoneDTO::class.java) } returns dto

        // When
        val result = sut.getDocument<RingtoneDTO, RingtoneBO>(action = { task }, mapper = { it.toDomain() })

        // Then
        assertTrue(result.isRight())
        assertEquals(dto.toDomain(), result.getOrNull())
    }

    @Test
    fun `GIVEN document does not deserialize WHEN getDocument THEN returns Left NotFound`() = runTest {
        // Given
        val snapshot = mockk<DocumentSnapshot>()
        val task = mockk<Task<DocumentSnapshot>>()
        coEvery { task.await() } returns snapshot
        every { snapshot.toObject(RingtoneDTO::class.java) } returns null

        // When
        val result = sut.getDocument<RingtoneDTO, RingtoneBO>(action = { task }, mapper = { it.toDomain() })

        // Then
        assertTrue(result.isLeft())
        assertTrue(result.leftOrNull() is ErrorBO.NotFound)
    }

    @Test
    fun `GIVEN task throws WHEN getDocument THEN returns Left Unknown`() = runTest {
        // Given
        val task = mockk<Task<DocumentSnapshot>>()
        coEvery { task.await() } throws RuntimeException("boom")

        // When
        val result = sut.getDocument<RingtoneDTO, RingtoneBO>(action = { task }, mapper = { it.toDomain() })

        // Then
        assertTrue(result.isLeft())
        assertTrue(result.leftOrNull() is ErrorBO.Unknown)
    }

    @Test
    fun `GIVEN task succeeds WHEN createDocument THEN returns null error`() = runTest {
        // Given
        val task = mockk<Task<Void>>()
        coEvery { task.await() } returns mockk()

        // When
        val result = sut.createDocument(task)

        // Then
        assertNull(result)
    }

    @Test
    fun `GIVEN task throws WHEN createDocument THEN returns Unknown error`() = runTest {
        // Given
        val task = mockk<Task<Void>>()
        coEvery { task.await() } throws RuntimeException("boom")

        // When
        val result = sut.createDocument(task)

        // Then
        assertTrue(result is ErrorBO.Unknown)
    }

    companion object {
        private const val AWAIT_FACADE = "kotlinx.coroutines.tasks.TasksKt"
    }
}
