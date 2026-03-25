package com.germandebustamante.ringtonemanager.di

import com.germandebustamante.ringtonemanager.core.model.di.DefaultDispatcherProvider
import com.germandebustamante.ringtonemanager.core.model.di.DispatcherProvider
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
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    single { FirebaseAnalytics.getInstance(get()) } bind FirebaseAnalytics::class
    single { FirebaseFirestore.getInstance() } bind FirebaseFirestore::class
    single { FirebaseAuth.getInstance() } bind FirebaseAuth::class

    single<DispatcherProvider> { DefaultDispatcherProvider() }

    factory { MultipleExoPlayerAdapter(get()) } bind MultiplePlayerAdapter::class
    factory { SingleExoPlayerAdapter(get()) } bind SinglePlayerAdapter::class

    single<Navigator> { DefaultNavigator() }
}

val viewModelModule = module {
    viewModelOf(::HomeViewModel)
    viewModel { (route: Destination.RingtoneDetailScreen) ->
        RingtoneDetailViewModel(
            route = route,
            playerAdapter = get(),
            fetchRingtoneDetailUseCase = get(),
            navigator = get(),
        )
    }
    viewModelOf(::RegisterViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::ForgotPasswordViewModel)
}
