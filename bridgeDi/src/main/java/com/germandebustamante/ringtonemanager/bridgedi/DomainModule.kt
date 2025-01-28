package com.germandebustamante.ringtonemanager.bridgedi

import com.germandebustamante.ringtonemanager.domain.ringtone.usecase.GetFullRingtonesUseCase
import com.germandebustamante.ringtonemanager.domain.ringtone.usecase.GetFullRingtonesUseCaseImpl
import com.germandebustamante.ringtonemanager.domain.ringtone.usecase.GetRingtoneDetailUseCase
import com.germandebustamante.ringtonemanager.domain.ringtone.usecase.GetRingtoneDetailUseCaseImpl
import org.koin.dsl.bind
import org.koin.dsl.module

val domainModule = module {
    factory { GetFullRingtonesUseCaseImpl(get()) } bind GetFullRingtonesUseCase::class
    factory { GetRingtoneDetailUseCaseImpl(get()) } bind GetRingtoneDetailUseCase::class
}