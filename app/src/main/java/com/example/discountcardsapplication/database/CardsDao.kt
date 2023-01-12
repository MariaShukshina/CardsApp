package com.example.discountcardsapplication.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.discountcardsapplication.models.Card

@Dao
interface CardsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCard(card: Card)

    @Update
    suspend fun updateCard(card: Card)

    @Delete
    suspend fun deleteCard(card: Card)

    @Query("SELECT * FROM card_information")
    fun getAllCards(): LiveData<List<Card>>
}