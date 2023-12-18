package com.mshukshina.discountcardsapplication.di

import android.app.Application
import androidx.room.Room
import com.mshukshina.discountcardsapplication.data.data_source.CardsDatabase
import com.mshukshina.discountcardsapplication.data.data_source.CardsDatabase.Companion.DATABASE_NAME
import com.mshukshina.discountcardsapplication.data.repositoryimpl.CardRepositoryImpl
import com.mshukshina.discountcardsapplication.domain.repository.CardRepository
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