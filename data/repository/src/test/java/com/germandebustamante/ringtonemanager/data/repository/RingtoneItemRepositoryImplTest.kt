package com.germandebustamante.ringtonemanager.data.repository

import arrow.core.left
import arrow.core.right
import com.germandebustamante.ringtonemanager.core.model.error.ErrorBOMother
import com.germandebustamante.ringtonemanager.core.model.ringtone.RingtoneBO
import com.germandebustamante.ringtonemanager.core.model.ringtone.RingtoneBOMother
import com.germandebustamante.ringtonemanager.data.datasource.RingtoneItemRemoteDataSource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MockKExtension::class)
class RingtoneItemRepositoryImplTest {

    private lateinit var sut: RingtoneItemRepositoryImpl

    @MockK
    private lateinit var remoteDataSource: RingtoneItemRemoteDataSource

    private val testDispatcher = UnconfinedTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        sut = RingtoneItemRepositoryImpl(remoteDataSource)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `GIVEN ringtone detail success WHEN getting ringtone detail THEN returns success with ringtone`() = runTest {
        // Given
        val expectedRingtone = RingtoneBOMother.random()
        givenGetRingtoneDetailSuccess(expectedRingtone)

        // When
        val result = sut.getRingtoneDetail(expectedRingtone.id)

        // Then
        assert(result.isRight())
        assertEquals(expectedRingtone, result.getOrNull())
        coVerify(exactly = 1) { remoteDataSource.getRingtoneDetail(expectedRingtone.id) }
    }

    @Test
    fun `GIVEN server error WHEN getting ringtone detail THEN returns error`() = runTest {
        // Given
        givenGetRingtoneDetailError(RINGTONE_ID)

        // When
        val result = sut.getRingtoneDetail(RINGTONE_ID)

        // Then
        assert(result.isLeft())
        assertEquals(ErrorBOMother.serverError(), result.leftOrNull())
        coVerify(exactly = 1) { remoteDataSource.getRingtoneDetail(RINGTONE_ID) }
    }

    @Test
    fun `GIVEN ringtone detail success WHEN getting ringtone detail THEN executes on IO dispatcher`() = runTest {
        // Given
        val ringtone = RingtoneBOMother.random()
        givenGetRingtoneDetailSuccess(ringtone)

        // When
        sut.getRingtoneDetail(ringtone.id)

        // Then
        coVerify(exactly = 1) { remoteDataSource.getRingtoneDetail(ringtone.id) }
    }

    //region General Stubs
    private fun givenGetRingtoneDetailSuccess(ringtone: RingtoneBO = RingtoneBOMother.random()) {
        coEvery { remoteDataSource.getRingtoneDetail(ringtone.id) } returns ringtone.right()
    }

    private fun givenGetRingtoneDetailError(ringtoneId: String) {
        coEvery { remoteDataSource.getRingtoneDetail(ringtoneId) } returns ErrorBOMother.serverError().left()
    }
    //endregion

    companion object {
        private const val RINGTONE_ID = "ringtone_123"
    }
}
