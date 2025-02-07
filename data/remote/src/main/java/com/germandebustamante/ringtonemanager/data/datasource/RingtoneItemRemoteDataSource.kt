package com.germandebustamante.ringtonemanager.data.datasource

import arrow.core.Either
import com.germandebustamante.ringtonemanager.domain.error.CustomError
import com.germandebustamante.ringtonemanager.domain.ringtone.model.RingtoneBO

interface RingtoneItemRemoteDataSource {
    suspend fun getRingtoneDetail(ringtoneId: String): Either<CustomError, RingtoneBO>
}