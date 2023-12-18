package com.mshukshina.discountcardsapplication.data.repositoryimpl

import androidx.lifecycle.LiveData
import com.mshukshina.discountcardsapplication.data.data_source.CardsDao
import com.mshukshina.discountcardsapplication.domain.models.Card
import com.mshukshina.discountcardsapplication.domain.repository.CardRepository

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