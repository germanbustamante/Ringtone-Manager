package com.germandebustamante.ringtonemanager.bridgedi

import com.germandebustamante.ringtonemanager.domain.authorization.usecase.ForgotPasswordUseCase
import com.germandebustamante.ringtonemanager.domain.authorization.usecase.GetUserFlowUseCase
import com.germandebustamante.ringtonemanager.domain.authorization.usecase.SignInUserUseCase
import com.germandebustamante.ringtonemanager.domain.authorization.usecase.SignOutUserUseCase
import com.germandebustamante.ringtonemanager.domain.authorization.usecase.SignUpUserUseCase
import com.germandebustamante.ringtonemanager.domain.ringtone.usecase.GetPopularRingtonesUseCase
import com.germandebustamante.ringtonemanager.domain.ringtone.usecase.GetRingtoneDetailUseCase
import com.germandebustamante.ringtonemanager.domain.ringtone.usecase.IncrementRingtonePopularityUseCase
import com.germandebustamante.ringtonemanager.domain.ringtone.usecase.LoadMoreRingtonesUseCase
import com.germandebustamante.ringtonemanager.domain.ringtone.usecase.SyncPopularRingtonesUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { GetPopularRingtonesUseCase(get()) }
    factory { SyncPopularRingtonesUseCase(get()) }
    factory { LoadMoreRingtonesUseCase(get()) }
    factory { GetRingtoneDetailUseCase(get(), get()) }
    factory { IncrementRingtonePopularityUseCase(get()) }
    factory { GetUserFlowUseCase(get()) }
    factory { SignUpUserUseCase(get()) }
    factory { SignInUserUseCase(get()) }
    factory { ForgotPasswordUseCase(get()) }
    factory { SignOutUserUseCase(get()) }
}