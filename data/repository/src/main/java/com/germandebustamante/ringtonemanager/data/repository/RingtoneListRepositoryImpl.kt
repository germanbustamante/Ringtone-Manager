package com.germandebustamante.ringtonemanager.data.repository

import arrow.core.Either
import com.germandebustamante.ringtonemanager.core.model.error.ErrorBO
import com.germandebustamante.ringtonemanager.core.model.ringtone.RingtoneBO
import com.germandebustamante.ringtonemanager.data.datasource.RingtoneListRemoteDataSource
import com.germandebustamante.ringtonemanager.domain.ringtone.repository.RingtoneListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class RingtoneListRepositoryImpl(
    remoteDataSource: RingtoneListRemoteDataSource,
) : RingtoneListRepository {

    override val popularRingtones: Flow<Either<ErrorBO, List<RingtoneBO>>> =
        remoteDataSource.getPopularRingtones().flowOn(Dispatchers.IO)
}