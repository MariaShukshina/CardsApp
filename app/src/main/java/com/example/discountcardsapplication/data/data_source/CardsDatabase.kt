package com.example.discountcardsapplication.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.discountcardsapplication.domain.models.Card

@Database(entities = [Card::class], version = 1)

abstract class CardsDatabase : RoomDatabase() {
    abstract fun getCardsDao(): CardsDao

    companion object {
        const val DATABASE_NAME = "CardsDB.db"
    }
}
