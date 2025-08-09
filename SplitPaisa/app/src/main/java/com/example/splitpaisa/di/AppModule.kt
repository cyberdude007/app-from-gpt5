
package com.example.splitpaisa.di

import android.content.Context
import com.example.splitpaisa.data.db.AppDatabase
import com.example.splitpaisa.data.db.DbProvider
import com.example.splitpaisa.data.repo.Repo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides @Singleton fun provideDb(@ApplicationContext ctx: Context): AppDatabase = DbProvider.create(ctx)
    @Provides @Singleton fun provideRepo(db: AppDatabase): Repo = Repo(db)
}
