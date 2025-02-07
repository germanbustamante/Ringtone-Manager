package com.germandebustamante.ringtonemanager.domain.ringtone.repository

import com.germandebustamante.ringtonemanager.domain.ringtone.model.RingtoneBO
import kotlinx.coroutines.flow.Flow

interface RingtoneListRepository {
    val popularRingtones: Flow<List<RingtoneBO>>
}