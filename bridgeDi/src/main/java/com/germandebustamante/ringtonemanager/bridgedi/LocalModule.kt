package com.germandebustamante.ringtonemanager.bridgedi

import com.germandebustamante.ringtonemanager.data.local.preferences.DataStoreUserPreferencesDataSource
import com.germandebustamante.ringtonemanager.data.local.preferences.UserPreferencesDataSource
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import org.koin.dsl.module

val localModule = module {
    single { DataStoreUserPreferencesDataSource(androidContext()) } bind UserPreferencesDataSource::class
}
