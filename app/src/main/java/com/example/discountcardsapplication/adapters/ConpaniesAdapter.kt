package com.example.discountcardsapplication.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.discountcardsapplication.databinding.CompanyItemBinding
import com.example.discountcardsapplication.models.Company

class Companiesdapter(): RecyclerView.Adapter<Companiesdapter.PopularMealsViewHolder>() {
    private var companiesList = listOf<Company>()
    //lateinit var onItemClick: (Company) -> Unit

    fun setMeals(companiesList: List<Company>){
        this.companiesList = companiesList
        notifyDataSetChanged()
    }

    class PopularMealsViewHolder(binding: CompanyItemBinding): RecyclerView.ViewHolder(binding.root){
        val companyAvatar = binding.companyAvatar
        val companyName = binding.tvCompanyName
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMealsViewHolder {
        return PopularMealsViewHolder(CompanyItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false))
    }

    override fun onBindViewHolder(holder: PopularMealsViewHolder, position: Int) {

        holder.companyAvatar.setImageResource(companiesList[position].image)
        holder.companyName.text = companiesList[position].name

        holder.itemView.setOnClickListener {
            //onItemClick.invoke(companiesList[position])
        }
    }

    override fun getItemCount(): Int {
        return companiesList.size
    }
}