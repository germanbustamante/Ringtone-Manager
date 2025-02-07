package com.germandebustamante.ringtonemanager.data.repository

import arrow.core.Either
import com.germandebustamante.ringtonemanager.data.datasource.RingtoneItemRemoteDataSource
import com.germandebustamante.ringtonemanager.domain.error.CustomError
import com.germandebustamante.ringtonemanager.domain.ringtone.model.RingtoneBO
import com.germandebustamante.ringtonemanager.domain.ringtone.repository.RingtoneItemRepository

class RingtoneItemRepositoryImpl(
    private val remoteDataSource: RingtoneItemRemoteDataSource,
) : RingtoneItemRepository {

    override suspend fun getRingtoneDetail(ringtoneId: String): Either<CustomError, RingtoneBO> =
        remoteDataSource.getRingtoneDetail(ringtoneId)
}