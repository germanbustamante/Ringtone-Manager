package com.germandebustamante.ringtonemanager.data.repository

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import com.germandebustamante.ringtonemanager.core.model.di.TestDispatcherProvider
import com.germandebustamante.ringtonemanager.core.model.error.ErrorBOMother
import com.germandebustamante.ringtonemanager.core.model.ringtone.RingtoneBOMother
import com.germandebustamante.ringtonemanager.data.datasource.RingtoneListPage
import com.germandebustamante.ringtonemanager.data.datasource.RingtoneListRemoteDataSource
import com.germandebustamante.ringtonemanager.data.local.db.dao.RingtoneDao
import com.germandebustamante.ringtonemanager.data.local.db.entity.RingtoneEntity
import com.germandebustamante.ringtonemanager.data.local.db.mapper.toEntity
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class RingtoneListRepositoryImplTest {

    @MockK
    private lateinit var remoteDataSource: RingtoneListRemoteDataSource

    @MockK
    private lateinit var ringtoneDao: RingtoneDao

    private fun buildSut() = RingtoneListRepositoryImpl(remoteDataSource, TestDispatcherProvider(), ringtoneDao)

    @Nested
    inner class PopularRingtones {

        @Test
        fun `GIVEN ringtones cached in Room WHEN accessing popular ringtones THEN emits mapped domain list`() = runTest {
            val expectedRingtones = RingtoneBOMother.randomList()
            every { ringtoneDao.observeAll() } returns flowOf(expectedRingtones.map { it.toEntity() })

            buildSut().popularRingtones.test {
                assertEquals(expectedRingtones, awaitItem())
                awaitComplete()
            }
        }

        @Test
        fun `GIVEN empty Room cache WHEN accessing popular ringtones THEN emits empty list`() = runTest {
            every { ringtoneDao.observeAll() } returns flowOf(emptyList<RingtoneEntity>())

            buildSut().popularRingtones.test {
                assertTrue(awaitItem().isEmpty())
                awaitComplete()
            }
        }
    }

    @Nested
    inner class SyncPopularRingtones {

        @BeforeEach
        fun setUp() {
            every { ringtoneDao.observeAll() } returns flowOf(emptyList())
        }

        @Test
        fun `GIVEN successful remote fetch WHEN syncing THEN upserts to Room and returns success`() = runTest {
            val ringtones = RingtoneBOMother.randomList()
            val page = RingtoneListPage(ringtones, nextCursor = null, hasMore = false)
            coEvery { remoteDataSource.fetchPopularRingtones(any(), any()) } returns page.right()
            coEvery { ringtoneDao.upsertAll(any()) } just Runs

            val result = buildSut().syncPopularRingtones()

            assertTrue(result.isRight())
            coVerify(exactly = 1) { ringtoneDao.upsertAll(ringtones.map { it.toEntity() }) }
        }

        @Test
        fun `GIVEN remote fetch error WHEN syncing THEN returns error without touching Room`() = runTest {
            val error = ErrorBOMother.serverError()
            coEvery { remoteDataSource.fetchPopularRingtones(any(), any()) } returns error.left()

            val result = buildSut().syncPopularRingtones()

            assertEquals(error, result.leftOrNull())
            coVerify(exactly = 0) { ringtoneDao.upsertAll(any()) }
        }

        @Test
        fun `GIVEN successful remote fetch WHEN syncing THEN error is null`() = runTest {
            val page = RingtoneListPage(RingtoneBOMother.randomList(), nextCursor = null, hasMore = false)
            coEvery { remoteDataSource.fetchPopularRingtones(any(), any()) } returns page.right()
            coEvery { ringtoneDao.upsertAll(any()) } just Runs

            val result = buildSut().syncPopularRingtones()

            assertNull(result.leftOrNull())
        }
    }

    @Nested
    inner class LoadMoreRingtones {

        @BeforeEach
        fun setUp() {
            every { ringtoneDao.observeAll() } returns flowOf(emptyList())
        }

        @Test
        fun `GIVEN more ringtones available WHEN loading more THEN returns hasMore true`() = runTest {
            val page = RingtoneListPage(RingtoneBOMother.randomList(), nextCursor = null, hasMore = true)
            coEvery { remoteDataSource.fetchPopularRingtones(any(), any()) } returns page.right()
            coEvery { ringtoneDao.upsertAll(any()) } just Runs

            val result = buildSut().loadMoreRingtones()

            assertEquals(true, result.getOrNull())
        }

        @Test
        fun `GIVEN last page reached WHEN loading more THEN returns hasMore false`() = runTest {
            val page = RingtoneListPage(RingtoneBOMother.randomList(), nextCursor = null, hasMore = false)
            coEvery { remoteDataSource.fetchPopularRingtones(any(), any()) } returns page.right()
            coEvery { ringtoneDao.upsertAll(any()) } just Runs

            val result = buildSut().loadMoreRingtones()

            assertEquals(false, result.getOrNull())
        }

        @Test
        fun `GIVEN remote error WHEN loading more THEN returns error`() = runTest {
            val error = ErrorBOMother.serverError()
            coEvery { remoteDataSource.fetchPopularRingtones(any(), any()) } returns error.left()

            val result = buildSut().loadMoreRingtones()

            assertEquals(error, result.leftOrNull())
        }
    }
}
