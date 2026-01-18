package com.germandebustamante.ringtonemanager.data.datasource

import arrow.core.Either
import com.germandebustamante.ringtonemanager.core.model.error.ErrorBO
import com.germandebustamante.ringtonemanager.core.model.ringtone.RingtoneBO
import kotlinx.coroutines.flow.Flow

interface RingtoneListRemoteDataSource {
    fun getPopularRingtones(): Flow<Either<ErrorBO, List<RingtoneBO>>>
}