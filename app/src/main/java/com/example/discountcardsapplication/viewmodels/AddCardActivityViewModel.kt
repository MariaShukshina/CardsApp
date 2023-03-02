package com.example.discountcardsapplication.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.discountcardsapplication.data.data_source.CardsDatabase
import com.example.discountcardsapplication.data.repositoryimpl.CardRepositoryImpl
import com.example.discountcardsapplication.domain.models.Card
import com.example.discountcardsapplication.domain.repository.CardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddCardActivityViewModel @Inject constructor(private val repository: CardRepository) : ViewModel() {

    fun insertCard(card: Card) = viewModelScope.launch {
        repository.insertCard(card)
    }
}
