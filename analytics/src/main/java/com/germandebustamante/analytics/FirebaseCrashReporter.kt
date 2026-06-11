package com.germandebustamante.analytics

import com.germandebustamante.ringtonemanager.domain.observability.CrashReporter
import com.google.firebase.crashlytics.FirebaseCrashlytics

class FirebaseCrashReporter(private val crashlytics: FirebaseCrashlytics) : CrashReporter {

    override fun log(message: String) {
        crashlytics.log(message)
    }

    override fun recordException(throwable: Throwable) {
        crashlytics.recordException(throwable)
    }

    override fun setCustomKey(key: String, value: String) {
        crashlytics.setCustomKey(key, value)
    }
}
