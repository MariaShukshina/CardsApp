package com.example.discountcardsapplication.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.discountcardsapplication.database.CardsDatabase
import com.example.discountcardsapplication.models.Card
import kotlinx.coroutines.launch

class AddCardActivityViewModel(private val cardsDatabase: CardsDatabase) : ViewModel() {

    fun insertCard(card: Card) = viewModelScope.launch {
        cardsDatabase.getCardsDao().insertCard(card)
    }

    fun updateCard(card: Card) = viewModelScope.launch {
        cardsDatabase.getCardsDao().updateCard(card)
    }
}
