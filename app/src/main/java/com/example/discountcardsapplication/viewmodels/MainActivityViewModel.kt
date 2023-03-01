package com.example.discountcardsapplication.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.discountcardsapplication.database.CardsDatabase
import com.example.discountcardsapplication.models.Card
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(private val cardsDatabase: CardsDatabase) : ViewModel() {

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
