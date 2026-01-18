package com.germandebustamante.ringtonemanager.domain.ringtone.repository

interface AnalyticsRepository {
    fun onRingtoneObtained(ringtoneId: String, ringtoneName: String)
}