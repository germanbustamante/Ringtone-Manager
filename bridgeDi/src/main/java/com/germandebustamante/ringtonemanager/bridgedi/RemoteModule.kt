package com.germandebustamante.ringtonemanager.bridgedi

import com.germandebustamante.ringtonemanager.data.datasource.AuthenticationRemoteDataSource
import com.germandebustamante.ringtonemanager.data.datasource.RingtoneItemRemoteDataSource
import com.germandebustamante.ringtonemanager.data.datasource.RingtoneListRemoteDataSource
import com.germandebustamante.ringtonemanager.data.remote.firebase.auth.FirebaseAuthenticationRemoteDataSourceImpl
import com.germandebustamante.ringtonemanager.data.remote.firebase.firestore.RingtoneItemFirestoreRemoteDataSourceImpl
import com.germandebustamante.ringtonemanager.data.remote.firebase.firestore.RingtoneListFirestoreRemoteDataSourceImpl
import com.germandebustamante.ringtonemanager.data.remote.manager.FirebaseAuthManager
import com.germandebustamante.ringtonemanager.data.remote.manager.FirestoreManager
import org.koin.dsl.bind
import org.koin.dsl.module

val remoteModule = module {
    single { FirestoreManager() }
    single { FirebaseAuthManager() }

    single { RingtoneItemFirestoreRemoteDataSourceImpl(get(), get()) } bind RingtoneItemRemoteDataSource::class
    single { RingtoneListFirestoreRemoteDataSourceImpl(get()) } bind RingtoneListRemoteDataSource::class
    single {
        FirebaseAuthenticationRemoteDataSourceImpl(get(), get(), get(), get())
    } bind AuthenticationRemoteDataSource::class
}
