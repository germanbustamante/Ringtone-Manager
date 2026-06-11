package com.germandebustamante.ringtonemanager.bridgedi

import com.germandebustamante.ringtonemanager.data.repository.AuthenticationRepositoryImpl
import com.germandebustamante.ringtonemanager.data.repository.RingtoneItemRepositoryImpl
import com.germandebustamante.ringtonemanager.data.repository.RingtoneListRepositoryImpl
import com.germandebustamante.ringtonemanager.domain.authorization.repository.AuthenticationRepository
import com.germandebustamante.ringtonemanager.domain.ringtone.repository.RingtoneItemRepository
import com.germandebustamante.ringtonemanager.domain.ringtone.repository.RingtoneListRepository
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {
    single { RingtoneItemRepositoryImpl(get(), get()) } bind RingtoneItemRepository::class
    single { RingtoneListRepositoryImpl(get(), get(), get()) } bind RingtoneListRepository::class
    single { AuthenticationRepositoryImpl(get(), get()) } bind AuthenticationRepository::class
}
