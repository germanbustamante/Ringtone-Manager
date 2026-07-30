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
import com.germandebustamante.ringtonemanager.utils.ringtone.RingtoneInstaller
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    single { FirebaseAnalytics.getInstance(get()) } bind FirebaseAnalytics::class
    single { FirebaseFirestore.getInstance() } bind FirebaseFirestore::class
    single { FirebaseAuth.getInstance() } bind FirebaseAuth::class
    single { FirebaseCrashlytics.getInstance() } bind FirebaseCrashlytics::class

    single<DispatcherProvider> { DefaultDispatcherProvider() }

    factory { MultipleExoPlayerAdapter(get()) } bind MultiplePlayerAdapter::class
    factory { SingleExoPlayerAdapter(get()) } bind SinglePlayerAdapter::class
    single { RingtoneInstaller(androidApplication()) }

    single<Navigator> { DefaultNavigator() }
}

val viewModelModule = module {
    viewModel {
        HomeViewModel(
            getPopularRingtonesUseCase = get(),
            syncPopularRingtonesUseCase = get(),
            loadMoreRingtonesUseCase = get(),
            getUserFlowUseCase = get(),
            observeFavoriteIdsUseCase = get(),
            toggleFavoriteUseCase = get(),
            player = get(),
            navigator = get(),
        )
    }
    viewModel { (route: Destination.RingtoneDetailScreen) ->
        RingtoneDetailViewModel(
            route = route,
            playerAdapter = get(),
            fetchRingtoneDetailUseCase = get(),
            incrementRingtonePopularityUseCase = get(),
            ringtoneInstaller = get(),
            getUserFlowUseCase = get(),
            observeFavoriteIdsUseCase = get(),
            toggleFavoriteUseCase = get(),
            navigator = get(),
        )
    }
    viewModel {
        RegisterViewModel(
            signUpUserUseCase = get(),
            currentUserFlowUseCase = get(),
            signInUserUseCase = get(),
            navigator = get(),
        )
    }
    viewModel {
        LoginViewModel(
            signInUserUseCase = get(),
            currentUserFlowUseCase = get(),
            navigator = get(),
        )
    }
    viewModel {
        SettingsViewModel(
            getUserFlowUseCase = get(),
            signOutUserUseCase = get(),
            changePasswordUseCase = get(),
            navigator = get(),
        )
    }
    viewModel {
        ForgotPasswordViewModel(
            navigator = get(),
            forgotPasswordUseCase = get(),
        )
    }
}
