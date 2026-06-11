package com.germandebustamante.ringtonemanager.data.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "user_preferences"
)

class DataStoreUserPreferencesDataSource(private val context: Context) : UserPreferencesDataSource {

    override val preferredLanguage: Flow<String?> =
        context.dataStore.data.map { prefs -> prefs[KEY_LANGUAGE] }

    override suspend fun setPreferredLanguage(languageTag: String) {
        context.dataStore.edit { prefs -> prefs[KEY_LANGUAGE] = languageTag }
    }

    companion object {
        private val KEY_LANGUAGE = stringPreferencesKey("preferred_language")
    }
}
