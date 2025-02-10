package com.germandebustamante.ringtonemanager.bridgedi

import com.germandebustamante.analytics.AnalyticsRepositoryImpl
import com.germandebustamante.ringtonemanager.domain.ringtone.repository.AnalyticsRepository
import org.koin.dsl.bind
import org.koin.dsl.module

val analyticsModule = module {
    factory { AnalyticsRepositoryImpl(get()) } bind AnalyticsRepository::class
}