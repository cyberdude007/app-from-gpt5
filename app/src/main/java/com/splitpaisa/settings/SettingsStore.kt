package com.splitpaisa.settings

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.Flow

private val Context.dataStore by preferencesDataStore(name = "settings")

object Keys {
    val PLAIN_MODE = booleanPreferencesKey("plain_mode")
    val SIMPLIFY = booleanPreferencesKey("simplify")
}

class SettingsStore(private val context: Context) {
    val plainMode: Flow<Boolean> = context.dataStore.data.map { it[Keys.PLAIN_MODE] ?: false }
    val simplify: Flow<Boolean> = context.dataStore.data.map { it[Keys.SIMPLIFY] ?: true }

    suspend fun setPlainMode(on: Boolean) = context.dataStore.edit { it[Keys.PLAIN_MODE] = on }
    suspend fun setSimplify(on: Boolean) = context.dataStore.edit { it[Keys.SIMPLIFY] = on }
}
