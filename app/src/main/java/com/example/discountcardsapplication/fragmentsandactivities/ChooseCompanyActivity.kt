package com.example.discountcardsapplication.fragmentsandactivities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.discountcardsapplication.adapters.CompaniesAdapter
import com.example.discountcardsapplication.databinding.ActivityChooseCompanyBinding
import com.example.discountcardsapplication.models.CompaniesList
import com.example.discountcardsapplication.models.Company
import com.example.discountcardsapplication.utils.FilterListUtil

class ChooseCompanyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChooseCompanyBinding
    private lateinit var companiesAdapter: CompaniesAdapter
    private var companiesList = listOf<Company>()
    private lateinit var companiesSearchView: SearchView
    private var isShowingNoData = false

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
        companiesList = CompaniesList.makeCompaniesList()

        binding.fabAddCard.setOnClickListener {
            startActivity(Intent(this, AddCardActivity::class.java))
        }

        companiesSearchView = binding.companiesSearchView
        companiesSearchView.clearFocus()
        companiesSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredCompaniesList = FilterListUtil.filteredCompaniesList(newText, companiesList)
                if (filteredCompaniesList.isEmpty() && !isShowingNoData) {
                    isShowingNoData = true
                    Toast.makeText(this@ChooseCompanyActivity, "No data found", Toast.LENGTH_SHORT).show()
                }
                if (filteredCompaniesList.isNotEmpty()) {
                    isShowingNoData = false
                }
                companiesAdapter.setCompaniesList(filteredCompaniesList)
                return true
            }
        })

        prepareCompaniesRecyclerView()
        setupInfoInRecyclerView()

        onCompanyClick()
    }

    private fun onCompanyClick() {
        companiesAdapter.onItemClick = {
            val intent = Intent(this, AddCardActivity::class.java)
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
    private fun setupInfoInRecyclerView() {
        companiesAdapter.setCompaniesList(companiesList)
    }
}
