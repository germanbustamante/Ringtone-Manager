package com.germandebustamante.ringtonemanager.bridgedi

import com.germandebustamante.ringtonemanager.domain.authorization.usecase.GetUserFlowUseCase
import com.germandebustamante.ringtonemanager.domain.authorization.usecase.GoogleSignInUseCase
import com.germandebustamante.ringtonemanager.domain.authorization.usecase.SignInUserUseCase
import com.germandebustamante.ringtonemanager.domain.authorization.usecase.SignOutUserUseCase
import com.germandebustamante.ringtonemanager.domain.authorization.usecase.SignUpUserUseCase
import com.germandebustamante.ringtonemanager.domain.authorization.usecase.impl.GetUserFlowUseCaseImpl
import com.germandebustamante.ringtonemanager.domain.authorization.usecase.impl.GoogleSignInImpl
import com.germandebustamante.ringtonemanager.domain.authorization.usecase.impl.SignInUserUseCaseImpl
import com.germandebustamante.ringtonemanager.domain.authorization.usecase.impl.SignOutUserUseCaseImpl
import com.germandebustamante.ringtonemanager.domain.authorization.usecase.impl.SignUpUserUseCaseImpl
import com.germandebustamante.ringtonemanager.domain.ringtone.usecase.GetPopularRingtonesUseCase
import com.germandebustamante.ringtonemanager.domain.ringtone.usecase.GetPopularRingtonesUseCaseImpl
import com.germandebustamante.ringtonemanager.domain.ringtone.usecase.GetRingtoneDetailUseCase
import com.germandebustamante.ringtonemanager.domain.ringtone.usecase.GetRingtoneDetailUseCaseImpl
import org.koin.dsl.bind
import org.koin.dsl.module

val domainModule = module {
    factory { GetPopularRingtonesUseCaseImpl(get()) } bind GetPopularRingtonesUseCase::class
    factory { GetRingtoneDetailUseCaseImpl(get(), get()) } bind GetRingtoneDetailUseCase::class
    factory { GetUserFlowUseCaseImpl(get()) } bind GetUserFlowUseCase::class
    factory { SignUpUserUseCaseImpl(get()) } bind SignUpUserUseCase::class
    factory { SignInUserUseCaseImpl(get()) } bind SignInUserUseCase::class
    factory { GoogleSignInImpl(get()) } bind GoogleSignInUseCase::class
    factory { SignOutUserUseCaseImpl(get()) } bind SignOutUserUseCase::class
}