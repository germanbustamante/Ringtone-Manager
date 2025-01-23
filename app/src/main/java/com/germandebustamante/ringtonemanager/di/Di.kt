package com.germandebustamante.ringtonemanager.di

import com.germandebustamante.ringtonemanager.ui.screen.home.HomeViewModel
import com.germandebustamante.ringtonemanager.ui.screen.ringtone.RingtoneDetailViewModel
import com.germandebustamante.ringtonemanager.utils.audio.MultipleExoPlayerAdapter
import com.germandebustamante.ringtonemanager.utils.audio.MultiplePlayerAdapter
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    factory { FirebaseFirestore.getInstance() } bind FirebaseFirestore::class
    factory { MultipleExoPlayerAdapter(get()) } bind MultiplePlayerAdapter::class
}

val viewModelModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::RingtoneDetailViewModel)
}