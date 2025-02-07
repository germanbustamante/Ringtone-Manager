package com.germandebustamante.ringtonemanager.data.datasource

import com.germandebustamante.ringtonemanager.domain.ringtone.model.RingtoneBO
import kotlinx.coroutines.flow.Flow

interface RingtoneListRemoteDataSource {
    fun getPopularRingtones(): Flow<List<RingtoneBO>>
}