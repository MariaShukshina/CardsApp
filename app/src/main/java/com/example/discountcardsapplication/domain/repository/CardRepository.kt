package com.example.discountcardsapplication.domain.repository

import androidx.lifecycle.LiveData
import com.example.discountcardsapplication.domain.models.Card

interface CardRepository {
    suspend fun insertCard(card: Card)

    suspend fun updateCard(card: Card)

    suspend fun deleteCard(card: Card)

    fun getAllCards(): LiveData<List<Card>>
}