package com.example.discountcardsapplication.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.discountcardsapplication.database.CardsDatabase

class AddCardActivityViewModelFactory(
    private val cardsDatabase: CardsDatabase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AddCardActivityViewModel(cardsDatabase) as T
    }
}
