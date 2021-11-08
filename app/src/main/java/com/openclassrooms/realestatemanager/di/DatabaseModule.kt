package com.openclassrooms.realestatemanager.di

import android.content.Context
import androidx.room.Room
import com.openclassrooms.realestatemanager.database.EstateRoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideEstateDao(database: EstateRoomDatabase) = database.EstateDAO()

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context, callback: EstateRoomDatabase.Callback) =
        Room.databaseBuilder(appContext, EstateRoomDatabase::class.java, "estate_database")
            .fallbackToDestructiveMigration()
            .addCallback(callback)
            .build()

    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())

}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope