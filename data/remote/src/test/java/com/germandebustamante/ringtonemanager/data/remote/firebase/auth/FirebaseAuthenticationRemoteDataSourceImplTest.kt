package com.germandebustamante.ringtonemanager.data.remote.firebase.auth

import arrow.core.right
import com.germandebustamante.ringtonemanager.core.model.error.ErrorBO
import com.germandebustamante.ringtonemanager.core.model.error.ErrorBOMother
import com.germandebustamante.ringtonemanager.data.remote.manager.FirebaseAuthManager
import com.germandebustamante.ringtonemanager.data.remote.manager.FirestoreManager
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkStatic
import io.mockk.unmockkAll
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
class FirebaseAuthenticationRemoteDataSourceImplTest {

    private lateinit var sut: FirebaseAuthenticationRemoteDataSourceImpl

    private val firebaseAuth = mockk<FirebaseAuth>()
    private val firestore = mockk<FirebaseFirestore>()
    private val authManager = mockk<FirebaseAuthManager>()
    private val firestoreManager = mockk<FirestoreManager>()

    @BeforeEach
    fun setUp() {
        mockkStatic(AWAIT_FACADE)
        sut = FirebaseAuthenticationRemoteDataSourceImpl(firebaseAuth, firestore, authManager, firestoreManager)
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `GIVEN auth manager succeeds WHEN signIn THEN returns Right Unit`() = runTest {
        // Given
        coEvery { authManager.execute(any()) } returns mockk<AuthResult>().right()

        // When
        val result = sut.signIn(EMAIL, PASSWORD)

        // Then
        assertTrue(result.isRight())
    }

    @Test
    fun `GIVEN auth manager fails WHEN signIn THEN returns Left with error`() = runTest {
        // Given
        val error = ErrorBOMother.invalidCredentials()
        coEvery { authManager.execute(any()) } returns arrow.core.Either.Left(error)

        // When
        val result = sut.signIn(EMAIL, PASSWORD)

        // Then
        assertTrue(result.isLeft())
        assertEquals(error, result.leftOrNull())
    }

    @Test
    fun `GIVEN auth manager succeeds and user saved WHEN googleSignIn THEN returns Right Unit`() = runTest {
        // Given
        coEvery { authManager.execute(any()) } returns authResultWithUser().right()
        givenUsersDocumentSetSucceeds()

        // When
        val result = sut.googleSignIn(GOOGLE_TOKEN)

        // Then
        assertTrue(result.isRight())
        coVerifyUserDocumentWritten()
    }

    @Test
    fun `GIVEN account created WHEN signUp THEN returns Right Unit and persists user`() = runTest {
        // Given
        mockkConstructor(UserProfileChangeRequest.Builder::class)
        val builder = mockk<UserProfileChangeRequest.Builder>()
        every { anyConstructed<UserProfileChangeRequest.Builder>().setDisplayName(any()) } returns builder
        every { builder.build() } returns mockk(relaxed = true)
        val authResult = authResultWithUser()
        val createTask = mockk<Task<AuthResult>>()
        every { firebaseAuth.createUserWithEmailAndPassword(EMAIL, PASSWORD) } returns createTask
        coEvery { createTask.await() } returns authResult
        givenUsersDocumentSetSucceeds()

        // When
        val result = sut.signUp(EMAIL, PASSWORD, NAME)

        // Then
        assertTrue(result.isRight())
        coVerifyUserDocumentWritten()
    }

    @Test
    fun `GIVEN sign up throws WHEN signUp THEN returns Left Unknown`() = runTest {
        // Given
        val createTask = mockk<Task<AuthResult>>()
        every { firebaseAuth.createUserWithEmailAndPassword(EMAIL, PASSWORD) } returns createTask
        coEvery { createTask.await() } throws RuntimeException("boom")

        // When
        val result = sut.signUp(EMAIL, PASSWORD, NAME)

        // Then
        assertTrue(result.isLeft())
        assertTrue(result.leftOrNull() is ErrorBO.Unknown)
    }

    @Test
    fun `GIVEN auth manager succeeds WHEN forgotPassword THEN returns Right Unit`() = runTest {
        // Given
        coEvery { authManager.executeVoid(any()) } returns Unit.right()

        // When
        val result = sut.forgotPassword(EMAIL)

        // Then
        assertTrue(result.isRight())
    }

    @Test
    fun `GIVEN auth manager fails WHEN forgotPassword THEN returns Left`() = runTest {
        // Given
        coEvery { authManager.executeVoid(any()) } returns arrow.core.Either.Left(ErrorBOMother.unknown())

        // When
        val result = sut.forgotPassword(EMAIL)

        // Then
        assertTrue(result.isLeft())
    }

    @Test
    fun `WHEN signOut THEN delegates to FirebaseAuth`() {
        // Given
        every { firebaseAuth.signOut() } just Runs

        // When
        sut.signOut()

        // Then
        verify(exactly = 1) { firebaseAuth.signOut() }
    }

    //region Helpers
    private fun authResultWithUser(): AuthResult {
        val user = mockk<FirebaseUser>()
        every { user.uid } returns USER_ID
        every { user.email } returns EMAIL
        every { user.displayName } returns NAME
        val updateProfileTask = mockk<Task<Void>>()
        every { user.updateProfile(any()) } returns updateProfileTask
        coEvery { updateProfileTask.await() } returns mockk()
        val authResult = mockk<AuthResult>()
        every { authResult.user } returns user
        return authResult
    }

    private fun givenUsersDocumentSetSucceeds() {
        val collection = mockk<CollectionReference>()
        val document = mockk<DocumentReference>()
        val setTask = mockk<Task<Void>>()
        every { firestore.collection(USERS_COLLECTION) } returns collection
        every { collection.document(USER_ID) } returns document
        every { document.set(any(), any()) } returns setTask
        coEvery { firestoreManager.createDocument(any()) } returns null
    }

    private fun coVerifyUserDocumentWritten() {
        verify(exactly = 1) { firestore.collection(USERS_COLLECTION) }
    }
    //endregion

    companion object {
        private const val EMAIL = "test@example.com"
        private const val PASSWORD = "password123"
        private const val NAME = "Test User"
        private const val GOOGLE_TOKEN = "google_token"
        private const val USER_ID = "user_1"
        private const val USERS_COLLECTION = "users_v1"
        private const val AWAIT_FACADE = "kotlinx.coroutines.tasks.TasksKt"
    }
}
