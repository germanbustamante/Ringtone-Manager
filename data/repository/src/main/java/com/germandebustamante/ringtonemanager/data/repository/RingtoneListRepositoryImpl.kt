package com.germandebustamante.ringtonemanager.data.repository

import arrow.core.Either
import com.germandebustamante.ringtonemanager.data.datasource.RingtoneListRemoteDataSource
import com.germandebustamante.ringtonemanager.domain.error.CustomError
import com.germandebustamante.ringtonemanager.domain.ringtone.model.RingtoneBO
import com.germandebustamante.ringtonemanager.domain.ringtone.repository.RingtoneListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class RingtoneListRepositoryImpl(
    remoteDataSource: RingtoneListRemoteDataSource,
) : RingtoneListRepository {

    override val popularRingtones: Flow<Either<CustomError, List<RingtoneBO>>> =
        remoteDataSource.getPopularRingtones().flowOn(Dispatchers.IO)

}