package com.germandebustamante.ringtonemanager.data.repository

import arrow.core.Either
import com.germandebustamante.ringtonemanager.core.model.error.ErrorBO
import com.germandebustamante.ringtonemanager.core.model.ringtone.RingtoneBO
import com.germandebustamante.ringtonemanager.data.datasource.RingtoneItemRemoteDataSource
import com.germandebustamante.ringtonemanager.domain.ringtone.repository.RingtoneItemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RingtoneItemRepositoryImpl(
    private val remoteDataSource: RingtoneItemRemoteDataSource,
) : RingtoneItemRepository {

    override suspend fun getRingtoneDetail(ringtoneId: String): Either<ErrorBO, RingtoneBO> =
        withContext(Dispatchers.IO) {
            remoteDataSource.getRingtoneDetail(ringtoneId)
        }
}