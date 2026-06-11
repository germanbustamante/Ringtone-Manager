package com.germandebustamante.ringtonemanager.data.repository

import arrow.core.Either
import arrow.core.right
import com.germandebustamante.ringtonemanager.core.model.di.DispatcherProvider
import com.germandebustamante.ringtonemanager.core.model.error.ErrorBO
import com.germandebustamante.ringtonemanager.core.model.ringtone.RingtoneBO
import com.germandebustamante.ringtonemanager.data.datasource.RingtoneListRemoteDataSource
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

    // Room is the single source of truth — the UI always observes the local cache.
    override val popularRingtones: Flow<List<RingtoneBO>> =
        ringtoneDao.observeAll().map { entities -> entities.map { it.toDomain() } }

    override suspend fun syncPopularRingtones(): Either<ErrorBO, Unit> =
        withContext(dispatcherProvider.io) {
            when (val result = remoteDataSource.fetchPopularRingtones()) {
                is Either.Left -> result
                is Either.Right -> {
                    ringtoneDao.upsertAll(result.value.map { it.toEntity() })
                    Unit.right()
                }
            }
        }
}
