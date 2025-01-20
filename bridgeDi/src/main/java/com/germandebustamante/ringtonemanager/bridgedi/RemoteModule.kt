package com.germandebustamante.ringtonemanager.bridgedi

import com.germandebustamante.ringtonemanager.data.datasource.RingtoneRemoteDataSource
import com.germandebustamante.ringtonemanager.data.remote.firestore.RingtoneFirestoreRemoteDataSourceImpl
import org.koin.dsl.bind
import org.koin.dsl.module

val remoteModule = module {
    factory { RingtoneFirestoreRemoteDataSourceImpl(get()) } bind RingtoneRemoteDataSource::class
}