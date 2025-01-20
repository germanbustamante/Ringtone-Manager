package com.germandebustamante.ringtonemanager.data.repository

import arrow.core.Either
import com.germandebustamante.ringtonemanager.data.datasource.RingtoneRemoteDataSource
import com.germandebustamante.ringtonemanager.domain.error.CustomError
import com.germandebustamante.ringtonemanager.domain.ringtone.model.RingtoneBO
import com.germandebustamante.ringtonemanager.domain.ringtone.repository.RingtoneRepository

class RingtoneRepositoryImpl(
    private val remoteDataSource: RingtoneRemoteDataSource,
) : RingtoneRepository {
    override suspend fun getFullRingtones(): Either<CustomError, List<RingtoneBO>> =
        remoteDataSource.getFullRingtones()
}