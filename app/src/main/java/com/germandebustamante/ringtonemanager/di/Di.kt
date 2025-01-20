package com.germandebustamante.ringtonemanager.di

import com.germandebustamante.ringtonemanager.ui.home.HomeViewModel
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module{
    factory { FirebaseFirestore.getInstance() } bind FirebaseFirestore::class
}

val viewModelModule = module {
    viewModelOf(::HomeViewModel)
}