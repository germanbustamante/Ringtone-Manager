package com.germandebustamante.ringtonemanager.bridgedi

import com.germandebustamante.ringtonemanager.domain.ringtone.usecase.GetFullRingtonesUseCase
import com.germandebustamante.ringtonemanager.domain.ringtone.usecase.GetFullRingtonesUseCaseImpl
import org.koin.dsl.bind
import org.koin.dsl.module

val domainModule = module {
    factory { GetFullRingtonesUseCaseImpl(get()) } bind GetFullRingtonesUseCase::class
}