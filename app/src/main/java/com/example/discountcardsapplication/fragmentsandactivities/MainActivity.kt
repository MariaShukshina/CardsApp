package com.example.discountcardsapplication.fragmentsandactivities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.discountcardsapplication.R
import com.example.discountcardsapplication.database.CardsDatabase
import com.example.discountcardsapplication.databinding.ActivityMainBinding
import com.example.discountcardsapplication.viewmodels.MainActivityViewModel
import com.example.discountcardsapplication.viewmodels.MainActivityViewModelFactory

class MainActivity : AppCompatActivity() {

    val viewModel: MainActivityViewModel by lazy {
        val cardsDatabase = CardsDatabase.getInstance(this)
        val factory = MainActivityViewModelFactory(cardsDatabase)
        ViewModelProvider(this, factory)[MainActivityViewModel::class.java]
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = Navigation.findNavController(this, R.id.host_fragment)
        NavigationUI.setupWithNavController(binding.bottomNav, navController)
    }
}