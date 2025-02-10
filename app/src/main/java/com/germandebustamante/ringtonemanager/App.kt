package com.germandebustamante.ringtonemanager

import android.app.Application
import com.germandebustamante.ringtonemanager.bridgedi.analyticsModule
import com.germandebustamante.ringtonemanager.bridgedi.domainModule
import com.germandebustamante.ringtonemanager.bridgedi.remoteModule
import com.germandebustamante.ringtonemanager.bridgedi.repositoryModule
import com.germandebustamante.ringtonemanager.di.appModule
import com.germandebustamante.ringtonemanager.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(appModule, viewModelModule, domainModule, repositoryModule, remoteModule, analyticsModule)
        }
    }
}