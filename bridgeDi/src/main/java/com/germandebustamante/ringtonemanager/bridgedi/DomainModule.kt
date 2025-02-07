package com.germandebustamante.ringtonemanager.bridgedi

import com.germandebustamante.ringtonemanager.domain.ringtone.usecase.GetPopularRingtonesUseCase
import com.germandebustamante.ringtonemanager.domain.ringtone.usecase.GetPopularRingtonesUseCaseImpl
import com.germandebustamante.ringtonemanager.domain.ringtone.usecase.GetRingtoneDetailUseCase
import com.germandebustamante.ringtonemanager.domain.ringtone.usecase.GetRingtoneDetailUseCaseImpl
import org.koin.dsl.bind
import org.koin.dsl.module

val domainModule = module {
    factory { GetPopularRingtonesUseCaseImpl(get()) } bind GetPopularRingtonesUseCase::class
    factory { GetRingtoneDetailUseCaseImpl(get()) } bind GetRingtoneDetailUseCase::class
}