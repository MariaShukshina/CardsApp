package com.mshukshina.discountcardsapplication.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mshukshina.discountcardsapplication.databinding.CompanyItemBinding
import com.mshukshina.discountcardsapplication.domain.models.Company

class CompaniesAdapter : RecyclerView.Adapter<CompaniesAdapter.CompaniesViewHolder>() {
    private var companiesList = listOf<Company>()
    lateinit var onItemClick: (Company) -> Unit

    fun setCompaniesList(companiesList: List<Company>) {
        this.companiesList = companiesList
        notifyDataSetChanged()
    }

    class CompaniesViewHolder(binding: CompanyItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val companyAvatar = binding.companyAvatar
        val companyName = binding.tvCompanyName
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompaniesViewHolder {
        return CompaniesViewHolder(
            CompanyItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CompaniesViewHolder, position: Int) {
        holder.companyAvatar.setImageResource(companiesList[position].image)
        holder.companyName.text = companiesList[position].name

        holder.itemView.setOnClickListener {
            onItemClick.invoke(companiesList[position])
        }
    }

    override fun getItemCount(): Int {
        return companiesList.size
    }
}
