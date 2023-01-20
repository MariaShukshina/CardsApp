package com.example.discountcardsapplication.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.discountcardsapplication.models.Card

@Database(entities = [Card::class], version = 1)

abstract class CardsDatabase: RoomDatabase() {
    abstract fun getCardsDao(): CardsDao

    companion object{
        @Volatile
        var INSTANCE: CardsDatabase? = null

        fun getInstance(context: Context): CardsDatabase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context,
                    CardsDatabase::class.java,
                    "CardsDB.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}