package com.splitpaisa.di

import android.content.Context
import com.splitpaisa.data.DefaultRepository
import com.splitpaisa.data.Repository
import com.splitpaisa.settings.SettingsStore
import com.splitpaisa.storage.AppDatabase

object ServiceLocator {
    fun repository(context: Context): Repository = DefaultRepository(AppDatabase.get(context))
    fun settings(context: Context): SettingsStore = SettingsStore(context)
}
