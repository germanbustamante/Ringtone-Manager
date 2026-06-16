package com.germandebustamante.ringtonemanager.data.repository

import arrow.core.Either
import arrow.core.right
import com.germandebustamante.ringtonemanager.core.model.di.DispatcherProvider
import com.germandebustamante.ringtonemanager.core.model.error.ErrorBO
import com.germandebustamante.ringtonemanager.core.model.ringtone.RingtoneBO
import com.germandebustamante.ringtonemanager.data.datasource.RingtoneListPage
import com.germandebustamante.ringtonemanager.data.datasource.RingtoneListRemoteDataSource
import com.germandebustamante.ringtonemanager.data.datasource.RingtonePageCursor
import com.germandebustamante.ringtonemanager.data.local.db.dao.RingtoneDao
import com.germandebustamante.ringtonemanager.data.local.db.mapper.toDomain
import com.germandebustamante.ringtonemanager.data.local.db.mapper.toEntity
import com.germandebustamante.ringtonemanager.domain.ringtone.repository.RingtoneListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class RingtoneListRepositoryImpl(
    private val remoteDataSource: RingtoneListRemoteDataSource,
    private val dispatcherProvider: DispatcherProvider,
    private val ringtoneDao: RingtoneDao,
) : RingtoneListRepository {

    // Cursor for the next Firestore page; null means start from the beginning.
    private var nextCursor: RingtonePageCursor? = null
    private var hasMore: Boolean = true

    // Room is the single source of truth — the UI always observes the local cache.
    override val popularRingtones: Flow<List<RingtoneBO>> =
        ringtoneDao.observeAll().map { entities -> entities.map { it.toDomain() } }

    override suspend fun syncPopularRingtones(): Either<ErrorBO, Unit> {
        nextCursor = null
        hasMore = true
        return fetchAndStore(cursor = null).map { }
    }

    override suspend fun loadMoreRingtones(): Either<ErrorBO, Boolean> {
        if (!hasMore) return true.right()
        return fetchAndStore(cursor = nextCursor).map { page -> page.hasMore }
    }

    private suspend fun fetchAndStore(cursor: RingtonePageCursor?): Either<ErrorBO, RingtoneListPage> =
        withContext(dispatcherProvider.io) {
            when (val result = remoteDataSource.fetchPopularRingtones(PAGE_SIZE, cursor)) {
                is Either.Left -> result
                is Either.Right -> {
                    val page = result.value
                    nextCursor = page.nextCursor
                    hasMore = page.hasMore
                    ringtoneDao.upsertAll(page.ringtones.map { it.toEntity() })
                    result
                }
            }
        }

    companion object {
        private const val PAGE_SIZE = 10
    }
}
