package com.example.discountcardsapplication.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.discountcardsapplication.database.CardsDatabase
import com.example.discountcardsapplication.models.Card
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddCardActivityViewModel @Inject constructor(private val cardsDatabase: CardsDatabase) : ViewModel() {

    fun insertCard(card: Card) = viewModelScope.launch {
        cardsDatabase.getCardsDao().insertCard(card)
    }
}
