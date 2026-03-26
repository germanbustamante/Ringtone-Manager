package com.germandebustamante.ringtonemanager.di

import com.germandebustamante.ringtonemanager.bridgedi.analyticsModule
import com.germandebustamante.ringtonemanager.bridgedi.domainModule
import com.germandebustamante.ringtonemanager.bridgedi.remoteModule
import com.germandebustamante.ringtonemanager.bridgedi.repositoryModule
import com.germandebustamante.ringtonemanager.core.model.di.DispatcherProvider
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.junit.jupiter.api.Test
import org.koin.dsl.module
import org.koin.test.verify.verify

class KoinModulesCheckTest {

    /**
     * Verifies that all bridgeDi modules can have their dependencies satisfied.
     * External types provided by appModule are listed in extraTypes so the static
     * analysis knows they will be available at runtime.
     */
    @Test
    fun `all bridgeDi modules resolve without missing dependencies`() {
        module {
            includes(analyticsModule, remoteModule, repositoryModule, domainModule)
        }.verify(
            extraTypes = listOf(
                FirebaseFirestore::class,
                FirebaseAuth::class,
                FirebaseAnalytics::class,
                DispatcherProvider::class,
            ),
        )
    }
}
