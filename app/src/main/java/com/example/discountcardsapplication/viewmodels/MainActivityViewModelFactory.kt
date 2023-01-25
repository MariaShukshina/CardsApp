package com.example.discountcardsapplication.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.discountcardsapplication.database.CardsDatabase

class MainActivityViewModelFactory(
    private val cardsDatabase: CardsDatabase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainActivityViewModel(cardsDatabase) as T
    }
}
