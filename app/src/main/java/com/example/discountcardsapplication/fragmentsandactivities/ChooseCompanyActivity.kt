package com.example.discountcardsapplication.fragmentsandactivities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.discountcardsapplication.R
import com.example.discountcardsapplication.adapters.Companiesdapter
import com.example.discountcardsapplication.databinding.ActivityChooseCompanyBinding
import com.example.discountcardsapplication.models.Company
import com.example.discountcardsapplication.models.Constants

class ChooseCompanyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChooseCompanyBinding
    private lateinit var companiesdapter: Companiesdapter
    private var companiesList = listOf<Company>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseCompanyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarSearchCompanies)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarSearchCompanies.setNavigationOnClickListener {
            onBackPressed()
        }
        companiesdapter = Companiesdapter()
        companiesList = Constants.makeShopList()

        prepareCompaniesRecyclerView()
        setupInfoInRecyclerView()
    }

    private fun prepareCompaniesRecyclerView() {
        binding.rvCompanies.apply {
            layoutManager = LinearLayoutManager(this@ChooseCompanyActivity, LinearLayoutManager.VERTICAL, false)
            adapter = companiesdapter
        }
    }
    private fun setupInfoInRecyclerView(){
        companiesdapter.setMeals(companiesList)
    }
}