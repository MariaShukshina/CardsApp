package com.example.discountcardsapplication.di

import android.app.Application
import androidx.room.Room
import com.example.discountcardsapplication.data.data_source.CardsDatabase
import com.example.discountcardsapplication.data.data_source.CardsDatabase.Companion.DATABASE_NAME
import com.example.discountcardsapplication.data.repositoryimpl.CardRepositoryImpl
import com.example.discountcardsapplication.domain.repository.CardRepository
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

    @Provides
    @Singleton
    fun provideCardRepository(db: CardsDatabase): CardRepository {
        return CardRepositoryImpl(db.getCardsDao())
    }
}