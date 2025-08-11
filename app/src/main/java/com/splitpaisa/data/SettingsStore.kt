package com.splitpaisa.data

import android.content.Context
import kotlinx.coroutines.flow.Flow

@Deprecated("Use com.splitpaisa.settings.SettingsStore; this class delegates to it to avoid divergence.")
class SettingsStore(private val context: Context) {
    private val impl = com.splitpaisa.settings.SettingsStore(context)
    val plainMode: Flow<Boolean> get() = impl.plainMode
    val simplify: Flow<Boolean> get() = impl.simplify
    suspend fun setPlainMode(on: Boolean) = impl.setPlainMode(on)
    suspend fun setSimplify(on: Boolean) = impl.setSimplify(on)
}
