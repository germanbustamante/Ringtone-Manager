package com.germandebustamante.ringtonemanager.data.repository

import arrow.core.Either
import com.germandebustamante.ringtonemanager.core.model.di.DispatcherProvider
import com.germandebustamante.ringtonemanager.core.model.error.ErrorBO
import com.germandebustamante.ringtonemanager.core.model.ringtone.RingtoneBO
import com.germandebustamante.ringtonemanager.data.datasource.RingtoneItemRemoteDataSource
import com.germandebustamante.ringtonemanager.domain.ringtone.repository.RingtoneItemRepository
import kotlinx.coroutines.withContext

class RingtoneItemRepositoryImpl(
    private val remoteDataSource: RingtoneItemRemoteDataSource,
    private val dispatcherProvider: DispatcherProvider,
) : RingtoneItemRepository {

    override suspend fun getRingtoneDetail(ringtoneId: String): Either<ErrorBO, RingtoneBO> =
        withContext(dispatcherProvider.io) {
            remoteDataSource.getRingtoneDetail(ringtoneId)
        }

    override suspend fun incrementPopularity(ringtoneId: String): Either<ErrorBO, Unit> =
        withContext(dispatcherProvider.io) {
            remoteDataSource.incrementPopularity(ringtoneId)
        }
}
