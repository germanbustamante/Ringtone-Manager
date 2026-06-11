package com.germandebustamante.ringtonemanager.data.remote.firebase.firestore

import com.germandebustamante.ringtonemanager.core.model.error.ErrorBO
import com.germandebustamante.ringtonemanager.data.remote.manager.FirestoreManager
import com.germandebustamante.ringtonemanager.data.remote.model.ringtone.RingtoneDTO
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import io.mockk.coEvery
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class RingtoneItemFirestoreRemoteDataSourceImplTest {

    private lateinit var sut: RingtoneItemFirestoreRemoteDataSourceImpl

    private val firestore = mockk<FirebaseFirestore>()
    private val collection = mockk<CollectionReference>()
    private val document = mockk<DocumentReference>()

    @BeforeEach
    fun setUp() {
        mockkStatic(AWAIT_FACADE)
        every { firestore.collection(COLLECTION_NAME) } returns collection
        every { collection.document(RINGTONE_ID) } returns document
        sut = RingtoneItemFirestoreRemoteDataSourceImpl(firestore, FirestoreManager())
    }

    @AfterEach
    fun tearDown() {
        unmockkStatic(AWAIT_FACADE)
    }

    @Test
    fun `GIVEN document exists WHEN getRingtoneDetail THEN returns Right with ringtone`() = runTest {
        // Given
        val dto = RingtoneDTO(id = RINGTONE_ID, name = "Ringtone", popularity = 5)
        val snapshot = mockk<DocumentSnapshot>()
        val task = mockk<Task<DocumentSnapshot>>()
        every { document.get() } returns task
        coEvery { task.await() } returns snapshot
        every { snapshot.toObject(RingtoneDTO::class.java) } returns dto

        // When
        val result = sut.getRingtoneDetail(RINGTONE_ID)

        // Then
        assertTrue(result.isRight())
        assertEquals(RINGTONE_ID, result.getOrNull()?.id)
    }

    @Test
    fun `GIVEN getRingtoneDetail does NOT mutate popularity`() = runTest {
        // Given
        val dto = RingtoneDTO(id = RINGTONE_ID, name = "Ringtone", popularity = 5)
        val snapshot = mockk<DocumentSnapshot>()
        val task = mockk<Task<DocumentSnapshot>>()
        every { document.get() } returns task
        coEvery { task.await() } returns snapshot
        every { snapshot.toObject(RingtoneDTO::class.java) } returns dto

        // When
        sut.getRingtoneDetail(RINGTONE_ID)

        // Then: reading the detail must NOT write back (no side effect)
        verify(exactly = 0) { document.update(any<String>(), any()) }
    }

    @Test
    fun `GIVEN update succeeds WHEN incrementPopularity THEN returns Right`() = runTest {
        // Given
        val task = mockk<Task<Void>>()
        every { document.update(POPULARITY_FIELD, any()) } returns task
        coEvery { task.await() } returns mockk()

        // When
        val result = sut.incrementPopularity(RINGTONE_ID)

        // Then
        assertTrue(result.isRight())
        verify(exactly = 1) { document.update(POPULARITY_FIELD, any()) }
    }

    @Test
    fun `GIVEN update throws WHEN incrementPopularity THEN returns Left Unknown`() = runTest {
        // Given
        val task = mockk<Task<Void>>()
        every { document.update(POPULARITY_FIELD, any()) } returns task
        coEvery { task.await() } throws RuntimeException("boom")

        // When
        val result = sut.incrementPopularity(RINGTONE_ID)

        // Then
        assertTrue(result.isLeft())
        assertTrue(result.leftOrNull() is ErrorBO.Unknown)
    }

    companion object {
        private const val COLLECTION_NAME = "ringtones_v1"
        private const val POPULARITY_FIELD = "popularity"
        private const val RINGTONE_ID = "ringtone_1"
        private const val AWAIT_FACADE = "kotlinx.coroutines.tasks.TasksKt"
    }
}
