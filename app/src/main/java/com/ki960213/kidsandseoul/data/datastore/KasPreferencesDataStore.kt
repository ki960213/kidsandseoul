package com.ki960213.kidsandseoul.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class KasPreferencesDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {

    val authentication: Flow<LocalAuthentication?> = dataStore.data
        .map { preferences ->
            val key = stringPreferencesKey(FIELD_AUTHENTICATION)
            Json.decodeFromString(preferences[key] ?: return@map null)
        }

    suspend fun updateAuthentication(authentication: LocalAuthentication) {
        dataStore.edit { preferences ->
            val key = stringPreferencesKey(FIELD_AUTHENTICATION)
            preferences[key] = Json.encodeToString(authentication)
        }
    }

    suspend fun removeAuthentication() {
        dataStore.edit { preferences ->
            val key = stringPreferencesKey(FIELD_AUTHENTICATION)
            preferences.remove(key)
        }
    }

    companion object {

        private const val FIELD_AUTHENTICATION = "authentication"
    }
}
