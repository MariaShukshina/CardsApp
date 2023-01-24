package com.example.discountcardsapplication.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.discountcardsapplication.database.CardsDatabase
import com.example.discountcardsapplication.models.Card
import kotlinx.coroutines.launch

class MainActivityViewModel(private val cardsDatabase: CardsDatabase) : ViewModel() {

    val getCards = cardsDatabase.getCardsDao().getAllCards()

    fun updateCard(card: Card) = viewModelScope.launch {
        cardsDatabase.getCardsDao().updateCard(card)
    }
    fun deleteCard(card: Card) = viewModelScope.launch {
        cardsDatabase.getCardsDao().deleteCard(card)
    }
    fun insertCard(card: Card) = viewModelScope.launch {
        cardsDatabase.getCardsDao().insertCard(card)
    }
}
