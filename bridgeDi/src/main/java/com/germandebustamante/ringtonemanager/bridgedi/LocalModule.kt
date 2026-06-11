package com.germandebustamante.ringtonemanager.bridgedi

import androidx.room.Room
import com.germandebustamante.ringtonemanager.data.local.db.RingtoneDatabase
import com.germandebustamante.ringtonemanager.data.local.preferences.DataStoreUserPreferencesDataSource
import com.germandebustamante.ringtonemanager.data.local.preferences.UserPreferencesDataSource
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import org.koin.dsl.module

val localModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            RingtoneDatabase::class.java,
            RingtoneDatabase.DATABASE_NAME,
        ).build()
    }
    single { get<RingtoneDatabase>().ringtoneDao() }

    single { DataStoreUserPreferencesDataSource(androidContext()) } bind UserPreferencesDataSource::class
}
