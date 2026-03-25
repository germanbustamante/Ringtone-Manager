package com.germandebustamante.ringtonemanager.data.repository

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import com.germandebustamante.ringtonemanager.core.model.di.TestDispatcherProvider
import com.germandebustamante.ringtonemanager.core.model.error.ErrorBOMother
import com.germandebustamante.ringtonemanager.core.model.ringtone.RingtoneBO
import com.germandebustamante.ringtonemanager.core.model.ringtone.RingtoneBOMother
import com.germandebustamante.ringtonemanager.data.datasource.RingtoneListRemoteDataSource
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class RingtoneListRepositoryImplTest {

    @MockK
    private lateinit var remoteDataSource: RingtoneListRemoteDataSource

    private fun buildSut() = RingtoneListRepositoryImpl(remoteDataSource, TestDispatcherProvider())

    @Test
    fun `GIVEN popular ringtones available WHEN accessing popular ringtones THEN returns success with ringtones`() = runTest {
        // Given
        val expectedRingtones = RingtoneBOMother.randomList()
        givenRemoteDataSourceSuccess(expectedRingtones)

        // When & Then
        buildSut().popularRingtones.test {
            val item = awaitItem()
            assertTrue(item.isRight())
            assertEquals(expectedRingtones, item.getOrNull())
            awaitComplete()
        }
    }

    @Test
    fun `GIVEN empty ringtones list WHEN accessing popular ringtones THEN returns success with empty list`() = runTest {
        // Given
        every { remoteDataSource.getPopularRingtones() } returns flowOf(emptyList<RingtoneBO>().right())

        // When & Then
        buildSut().popularRingtones.test {
            val item = awaitItem()
            assertTrue(item.isRight())
            assertTrue(item.getOrNull()?.isEmpty() == true)
            awaitComplete()
        }
    }

    @Test
    fun `GIVEN server error WHEN accessing popular ringtones THEN returns error`() = runTest {
        // Given
        givenRemoteDataSourceError()

        // When & Then
        buildSut().popularRingtones.test {
            val item = awaitItem()
            assertTrue(item.isLeft())
            assertEquals(ErrorBOMother.serverError(), item.leftOrNull())
            awaitComplete()
        }
    }

    //region Stubs
    private fun givenRemoteDataSourceSuccess(ringtones: List<RingtoneBO> = RingtoneBOMother.randomList()) {
        every { remoteDataSource.getPopularRingtones() } returns flowOf(ringtones.right())
    }

    private fun givenRemoteDataSourceError() {
        every { remoteDataSource.getPopularRingtones() } returns flowOf(ErrorBOMother.serverError().left())
    }
    //endregion
}
