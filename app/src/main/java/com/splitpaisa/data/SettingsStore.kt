package com.splitpaisa.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "splitpaisa_prefs")

data class GamificationState(
    val streakDays: Int = 0,
    val lastActiveEpochDays: Int = 0,
    val xp: Int = 0,
    val freezeMonth: String? = null
)

class SettingsStore private constructor(private val app: Context) {
    object Keys {
        val plainMode = booleanPreferencesKey("plainMode")
        val simplifyDebts = booleanPreferencesKey("simplifyDebts")
        val streakDays = intPreferencesKey("streakDays")
        val lastActiveEpochDays = intPreferencesKey("lastActive")
        val xp = intPreferencesKey("xp")
        val freezeMonth = stringPreferencesKey("freezeMonth")
    }

    val plainModeFlow: Flow<Boolean> = app.dataStore.data.map { it[Keys.plainMode] ?: false }
    val simplifyDebtsFlow: Flow<Boolean> = app.dataStore.data.map { it[Keys.simplifyDebts] ?: true }

    suspend fun setPlainMode(v: Boolean) { app.dataStore.edit { it[Keys.plainMode] = v } }
    suspend fun setSimplifyDebts(v: Boolean) { app.dataStore.edit { it[Keys.simplifyDebts] = v } }

    suspend fun readGamification(): GamificationState {
        val s = app.dataStore.data.first()
        return GamificationState(
            streakDays = s[Keys.streakDays] ?: 0,
            lastActiveEpochDays = s[Keys.lastActiveEpochDays] ?: 0,
            xp = s[Keys.xp] ?: 0,
            freezeMonth = s[Keys.freezeMonth]
        )
    }
    suspend fun writeGamification(g: GamificationState) {
        app.dataStore.edit {
            it[Keys.streakDays] = g.streakDays
            it[Keys.lastActiveEpochDays] = g.lastActiveEpochDays
            it[Keys.xp] = g.xp
        }
    }
    suspend fun writeFreezeMonth(m: String) { app.dataStore.edit { it[Keys.freezeMonth] = m } }

    companion object {
        @Volatile private var I: SettingsStore? = null
        fun get(context: Context): SettingsStore = I ?: synchronized(this) {
            I ?: SettingsStore(context.applicationContext).also { I = it }
        }
    }
}
