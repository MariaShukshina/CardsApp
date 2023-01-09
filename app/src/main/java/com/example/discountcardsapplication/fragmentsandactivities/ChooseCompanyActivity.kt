package com.example.discountcardsapplication.fragmentsandactivities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.discountcardsapplication.adapters.CompaniesAdapter
import com.example.discountcardsapplication.databinding.ActivityChooseCompanyBinding
import com.example.discountcardsapplication.models.Company
import com.example.discountcardsapplication.models.Constants

class ChooseCompanyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChooseCompanyBinding
    private lateinit var companiesAdapter: CompaniesAdapter
    private var companiesList = listOf<Company>()

    companion object {
        const val COMPANY_NAME = "COMPANY_NAME"
        const val COMPANY_IMAGE = "COMPANY_IMAGE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseCompanyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarSearchCompanies)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarSearchCompanies.setNavigationOnClickListener {
            onBackPressed()
        }
        companiesAdapter = CompaniesAdapter()
        companiesList = Constants.makeShopList()

        binding.fabAddCard.setOnClickListener {
            startActivity(Intent(this, AddOrEditCardActivity::class.java))
        }

        prepareCompaniesRecyclerView()
        setupInfoInRecyclerView()

        onCompanyClick()
    }

    private fun onCompanyClick() {
        companiesAdapter.onItemClick = {
            val intent = Intent(this, AddOrEditCardActivity::class.java)
            intent.putExtra(COMPANY_NAME, it.name)
            intent.putExtra(COMPANY_IMAGE, it.image)
            startActivity(intent)
        }
    }

    private fun prepareCompaniesRecyclerView() {
        binding.rvCompanies.apply {
            layoutManager = LinearLayoutManager(this@ChooseCompanyActivity, LinearLayoutManager.VERTICAL, false)
            adapter = companiesAdapter
        }
    }
    private fun setupInfoInRecyclerView(){
        companiesAdapter.setCompaniesList(companiesList)
    }
}