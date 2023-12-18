package com.mshukshina.discountcardsapplication.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mshukshina.discountcardsapplication.domain.models.Card
import com.mshukshina.discountcardsapplication.domain.repository.CardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddCardActivityViewModel @Inject constructor(private val repository: CardRepository) : ViewModel() {

    fun insertCard(card: Card) = viewModelScope.launch {
        repository.insertCard(card)
    }
}
