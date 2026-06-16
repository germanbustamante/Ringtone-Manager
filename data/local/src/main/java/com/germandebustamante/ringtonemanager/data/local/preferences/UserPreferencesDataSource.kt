package com.germandebustamante.ringtonemanager.data.local.preferences

import kotlinx.coroutines.flow.Flow

interface UserPreferencesDataSource {
    val preferredLanguage: Flow<String?>

    suspend fun setPreferredLanguage(languageTag: String)
}
