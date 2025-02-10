package com.germandebustamante.analytics

import com.germandebustamante.ringtonemanager.domain.ringtone.repository.AnalyticsRepository
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent

class AnalyticsRepositoryImpl(private val firebaseAnalytics: FirebaseAnalytics) : AnalyticsRepository {

    override fun onRingtoneObtained(ringtoneId: String, ringtoneName: String) {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM) {
            param(FirebaseAnalytics.Param.ITEM_ID, ringtoneId)
            param(FirebaseAnalytics.Param.ITEM_NAME, ringtoneName)
            param(FirebaseAnalytics.Param.CONTENT_TYPE, CONTENT_TYPE_RINGTONE)
        }
    }

    companion object {
        private const val CONTENT_TYPE_RINGTONE = "ringtone"
    }
}