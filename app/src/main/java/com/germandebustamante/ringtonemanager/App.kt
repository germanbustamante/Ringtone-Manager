package com.germandebustamante.ringtonemanager

import android.app.Application
import android.content.pm.ApplicationInfo
import com.germandebustamante.ringtonemanager.bridgedi.analyticsModule
import com.germandebustamante.ringtonemanager.bridgedi.domainModule
import com.germandebustamante.ringtonemanager.bridgedi.localModule
import com.germandebustamante.ringtonemanager.bridgedi.remoteModule
import com.germandebustamante.ringtonemanager.bridgedi.repositoryModule
import com.germandebustamante.ringtonemanager.di.appModule
import com.germandebustamante.ringtonemanager.di.viewModelModule
import com.germandebustamante.ringtonemanager.domain.observability.CrashReporter
import com.google.firebase.Firebase
import com.google.firebase.appcheck.appCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {

    private val crashReporter: CrashReporter by inject()

    override fun onCreate() {
        super.onCreate()

        installAppCheck()

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(appModule, viewModelModule, domainModule, repositoryModule, remoteModule, analyticsModule, localModule)
        }

        crashReporter.log("Application initialized")
    }

    /**
     * Debug builds use the debug provider (token registered in the Firebase
     * console); release builds attest with Play Integrity so only genuine,
     * unmodified app installs can reach the Firebase backend.
     */
    private fun installAppCheck() {
        val factory = if (isDebuggable()) {
            DebugAppCheckProviderFactory.getInstance()
        } else {
            PlayIntegrityAppCheckProviderFactory.getInstance()
        }
        Firebase.appCheck.installAppCheckProviderFactory(factory)
    }

    private fun isDebuggable(): Boolean =
        (applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
}