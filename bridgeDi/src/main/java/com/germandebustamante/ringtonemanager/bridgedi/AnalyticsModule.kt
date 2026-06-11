package com.germandebustamante.ringtonemanager.bridgedi

import com.germandebustamante.analytics.AnalyticsRepositoryImpl
import com.germandebustamante.analytics.FirebaseCrashReporter
import com.germandebustamante.ringtonemanager.domain.observability.CrashReporter
import com.germandebustamante.ringtonemanager.domain.ringtone.repository.AnalyticsRepository
import org.koin.dsl.bind
import org.koin.dsl.module

val analyticsModule = module {
    factory { AnalyticsRepositoryImpl(get()) } bind AnalyticsRepository::class
    single { FirebaseCrashReporter(get()) } bind CrashReporter::class
}