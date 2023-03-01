package com.example.discountcardsapplication.di

import android.app.Application
import androidx.room.Room
import com.example.discountcardsapplication.database.CardsDatabase
import com.example.discountcardsapplication.database.CardsDatabase.Companion.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun getDatabaseInstance(context: Application) = Room.databaseBuilder(
        context,
        CardsDatabase::class.java,
        DATABASE_NAME
    ).build()
}