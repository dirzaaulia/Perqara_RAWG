package com.dirzaaulia.perqararawg.di

import android.content.Context
import com.dirzaaulia.perqararawg.database.AppDatabase
import com.dirzaaulia.perqararawg.database.DatabaseDao
import com.dirzaaulia.perqararawg.network.Service
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    fun provideDatabaseDao(appDatabase: AppDatabase): DatabaseDao {
        return appDatabase.databaseDao()
    }

    @Singleton
    @Provides
    fun provideService(@ApplicationContext context: Context): Service {
        return Service.create(context)
    }
}