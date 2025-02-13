package com.germandebustamante.ringtonemanager.bridgedi

import com.germandebustamante.ringtonemanager.data.datasource.AuthenticationRemoteDataSource
import com.germandebustamante.ringtonemanager.data.datasource.RingtoneItemRemoteDataSource
import com.germandebustamante.ringtonemanager.data.datasource.RingtoneListRemoteDataSource
import com.germandebustamante.ringtonemanager.data.remote.firebase.auth.FirebaseAuthenticationRemoteDataSourceImpl
import com.germandebustamante.ringtonemanager.data.remote.firebase.firestore.RingtoneItemFirestoreRemoteDataSourceImpl
import com.germandebustamante.ringtonemanager.data.remote.firebase.firestore.RingtoneListFirestoreRemoteDataSourceImpl
import org.koin.dsl.bind
import org.koin.dsl.module

val remoteModule = module {
    factory { RingtoneItemFirestoreRemoteDataSourceImpl(get()) } bind RingtoneItemRemoteDataSource::class
    factory { RingtoneListFirestoreRemoteDataSourceImpl(get()) } bind RingtoneListRemoteDataSource::class
    factory { FirebaseAuthenticationRemoteDataSourceImpl(get()) } bind AuthenticationRemoteDataSource::class
}