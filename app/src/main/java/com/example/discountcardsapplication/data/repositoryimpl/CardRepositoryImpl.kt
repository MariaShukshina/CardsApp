package com.example.discountcardsapplication.data.repositoryimpl

import androidx.lifecycle.LiveData
import com.example.discountcardsapplication.data.data_source.CardsDao
import com.example.discountcardsapplication.domain.models.Card
import com.example.discountcardsapplication.domain.repository.CardRepository

class CardRepositoryImpl(private val dao: CardsDao): CardRepository {
    override suspend fun insertCard(card: Card) {
        dao.insertCard(card)
    }

    override suspend fun updateCard(card: Card) {
        dao.updateCard(card)
    }

    override suspend fun deleteCard(card: Card) {
        dao.deleteCard(card)
    }

    override fun getAllCards(): LiveData<List<Card>> {
        return dao.getAllCards()
    }
}