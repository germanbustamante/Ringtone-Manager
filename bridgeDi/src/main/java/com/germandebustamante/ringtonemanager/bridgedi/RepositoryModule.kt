package com.germandebustamante.ringtonemanager.bridgedi

import com.germandebustamante.ringtonemanager.data.repository.RingtoneItemRepositoryImpl
import com.germandebustamante.ringtonemanager.data.repository.RingtoneListRepositoryImpl
import com.germandebustamante.ringtonemanager.domain.ringtone.repository.RingtoneItemRepository
import com.germandebustamante.ringtonemanager.domain.ringtone.repository.RingtoneListRepository
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {
    factory { RingtoneItemRepositoryImpl(get()) } bind RingtoneItemRepository::class
    factory { RingtoneListRepositoryImpl(get()) } bind RingtoneListRepository::class
}