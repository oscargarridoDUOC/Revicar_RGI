package com.example.revicar_rgi.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore("user_prefs")

class SessionManager(private val context: Context) {
    companion object {
        private val USER_UID = stringPreferencesKey("user_uid")
    }

    val userUidFlow: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[USER_UID]
    }

    suspend fun saveUid(uid: String) {
        context.dataStore.edit { prefs -> prefs[USER_UID] = uid }
    }

    suspend fun clearSession() {
        context.dataStore.edit { prefs -> prefs.remove(USER_UID) }
    }
}