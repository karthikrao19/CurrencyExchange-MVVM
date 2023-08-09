package com.krtk.currencyexchange.dataLayer.local

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

//Annotate the AppDatabase class with @Database and add @Singleton annotation to the
// @Provides method in your Hilt module for the database.
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "ExchangeCenter"
        ).build()
    }

    @Provides
    @Singleton
    fun provideUserDao(database: AppDatabase): RoomDao {
        return database.roomDao()
    }
}