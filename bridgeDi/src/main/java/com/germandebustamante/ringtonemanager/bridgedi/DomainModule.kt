package com.germandebustamante.ringtonemanager.bridgedi

import com.germandebustamante.ringtonemanager.domain.authorization.usecase.ForgotPasswordUseCase
import com.germandebustamante.ringtonemanager.domain.authorization.usecase.GetUserFlowUseCase
import com.germandebustamante.ringtonemanager.domain.authorization.usecase.SignInUserUseCase
import com.germandebustamante.ringtonemanager.domain.authorization.usecase.SignOutUserUseCase
import com.germandebustamante.ringtonemanager.domain.authorization.usecase.SignUpUserUseCase
import com.germandebustamante.ringtonemanager.domain.ringtone.usecase.GetPopularRingtonesUseCase
import com.germandebustamante.ringtonemanager.domain.ringtone.usecase.GetRingtoneDetailUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { GetPopularRingtonesUseCase(get()) }
    factory { GetRingtoneDetailUseCase(get(), get()) }
    factory { GetUserFlowUseCase(get()) }
    factory { SignUpUserUseCase(get()) }
    factory { SignInUserUseCase(get()) }
    factory { ForgotPasswordUseCase(get()) }
    factory { SignOutUserUseCase(get()) }
}