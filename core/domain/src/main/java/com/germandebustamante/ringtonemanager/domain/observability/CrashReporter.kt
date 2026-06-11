package com.germandebustamante.ringtonemanager.domain.observability

/**
 * Abstraction over crash and non-fatal reporting so the rest of the app does not
 * depend on a concrete crash-reporting SDK. Implemented in :analytics.
 */
interface CrashReporter {
    fun log(message: String)
    fun recordException(throwable: Throwable)
    fun setCustomKey(key: String, value: String)
}
