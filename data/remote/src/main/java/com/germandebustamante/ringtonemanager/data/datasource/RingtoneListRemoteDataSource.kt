package com.germandebustamante.ringtonemanager.data.datasource

import arrow.core.Either
import com.germandebustamante.ringtonemanager.core.model.error.ErrorBO
import com.germandebustamante.ringtonemanager.core.model.ringtone.RingtoneBO

interface RingtoneListRemoteDataSource {
    suspend fun fetchPopularRingtones(): Either<ErrorBO, List<RingtoneBO>>
}
