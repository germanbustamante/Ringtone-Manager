package com.germandebustamante.ringtonemanager.bridgedi

import com.germandebustamante.ringtonemanager.data.repository.RingtoneRepositoryImpl
import com.germandebustamante.ringtonemanager.domain.ringtone.repository.RingtoneRepository
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {
    factory { RingtoneRepositoryImpl(get()) } bind RingtoneRepository::class
}