package com.germandebustamante.ringtonemanager

import android.app.Application
import com.germandebustamante.ringtonemanager.bridgedi.analyticsModule
import com.germandebustamante.ringtonemanager.bridgedi.domainModule
import com.germandebustamante.ringtonemanager.bridgedi.remoteModule
import com.germandebustamante.ringtonemanager.bridgedi.repositoryModule
import com.germandebustamante.ringtonemanager.di.appModule
import com.germandebustamante.ringtonemanager.di.viewModelModule
import com.germandebustamante.ringtonemanager.domain.observability.CrashReporter
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {

    private val crashReporter: CrashReporter by inject()

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(appModule, viewModelModule, domainModule, repositoryModule, remoteModule, analyticsModule)
        }

        crashReporter.log("Application initialized")
    }
}