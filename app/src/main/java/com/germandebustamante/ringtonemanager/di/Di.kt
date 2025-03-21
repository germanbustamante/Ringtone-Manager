package com.germandebustamante.ringtonemanager.di

import com.germandebustamante.ringtonemanager.core.navigation.action.DefaultNavigator
import com.germandebustamante.ringtonemanager.core.navigation.action.Navigator
import com.germandebustamante.ringtonemanager.core.navigation.destination.Destination
import com.germandebustamante.ringtonemanager.ui.screen.forgotpassword.ForgotPasswordViewModel
import com.germandebustamante.ringtonemanager.ui.screen.home.HomeViewModel
import com.germandebustamante.ringtonemanager.ui.screen.login.LoginViewModel
import com.germandebustamante.ringtonemanager.ui.screen.register.RegisterViewModel
import com.germandebustamante.ringtonemanager.ui.screen.ringtone.RingtoneDetailViewModel
import com.germandebustamante.ringtonemanager.ui.screen.settings.SettingsViewModel
import com.germandebustamante.ringtonemanager.utils.audio.MultipleExoPlayerAdapter
import com.germandebustamante.ringtonemanager.utils.audio.MultiplePlayerAdapter
import com.germandebustamante.ringtonemanager.utils.audio.SingleExoPlayerAdapter
import com.germandebustamante.ringtonemanager.utils.audio.SinglePlayerAdapter
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    factory { FirebaseAnalytics.getInstance(get()) } bind FirebaseAnalytics::class
    factory { FirebaseFirestore.getInstance() } bind FirebaseFirestore::class
    factory { FirebaseAuth.getInstance() } bind FirebaseAuth::class

    factory { MultipleExoPlayerAdapter(get()) } bind MultiplePlayerAdapter::class
    factory { SingleExoPlayerAdapter(get()) } bind SinglePlayerAdapter::class

    single<Navigator> { DefaultNavigator(startDestination = Destination.HomeScreen) }
}

val viewModelModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::RingtoneDetailViewModel)
    viewModelOf(::RegisterViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::ForgotPasswordViewModel)
}