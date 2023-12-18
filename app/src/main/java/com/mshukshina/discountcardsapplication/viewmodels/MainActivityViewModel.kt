package com.mshukshina.discountcardsapplication.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mshukshina.discountcardsapplication.domain.models.Card
import com.mshukshina.discountcardsapplication.domain.repository.CardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(private val repository: CardRepository) : ViewModel() {

    val getCards = repository.getAllCards()

    fun updateCard(card: Card) = viewModelScope.launch {
        repository.updateCard(card)
    }
    fun deleteCard(card: Card) = viewModelScope.launch {
        repository.deleteCard(card)
    }
    fun insertCard(card: Card) = viewModelScope.launch {
        repository.insertCard(card)
    }
}
