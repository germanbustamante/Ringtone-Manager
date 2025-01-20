package com.germandebustamante.ringtonemanager.data.datasource

import arrow.core.Either
import com.germandebustamante.ringtonemanager.domain.error.CustomError
import com.germandebustamante.ringtonemanager.domain.ringtone.model.RingtoneBO

interface RingtoneRemoteDataSource {
    suspend fun getFullRingtones(): Either<CustomError, List<RingtoneBO>>
}