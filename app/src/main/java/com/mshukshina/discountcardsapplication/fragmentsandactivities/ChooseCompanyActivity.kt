package com.mshukshina.discountcardsapplication.fragmentsandactivities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.mshukshina.discountcardsapplication.adapters.CompaniesAdapter
import com.mshukshina.discountcardsapplication.databinding.ActivityChooseCompanyBinding
import com.mshukshina.discountcardsapplication.domain.models.CompaniesList
import com.mshukshina.discountcardsapplication.domain.models.Company
import com.mshukshina.discountcardsapplication.domain.utils.FilterListUtil

class ChooseCompanyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChooseCompanyBinding
    private lateinit var companiesAdapter: CompaniesAdapter
    private var companiesList = listOf<Company>()
    private lateinit var companiesSearchView: SearchView
    private var isShowingData = true

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
                if (filteredCompaniesList.isEmpty() && isShowingData) {
                    isShowingData = false
                    Toast.makeText(this@ChooseCompanyActivity, "No data found", Toast.LENGTH_SHORT).show()
                }
                if (filteredCompaniesList.isNotEmpty()) {
                    isShowingData = true
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

    companion object {
        const val COMPANY_NAME = "COMPANY_NAME"
        const val COMPANY_IMAGE = "COMPANY_IMAGE"
    }
}
