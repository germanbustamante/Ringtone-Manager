package com.germandebustamante.ringtonemanager.data.datasource

import arrow.core.Either
import com.germandebustamante.ringtonemanager.domain.error.CustomError
import com.germandebustamante.ringtonemanager.domain.ringtone.model.RingtoneBO
import kotlinx.coroutines.flow.Flow

interface RingtoneListRemoteDataSource {
    fun getPopularRingtones(): Flow<Either<CustomError, List<RingtoneBO>>>
}