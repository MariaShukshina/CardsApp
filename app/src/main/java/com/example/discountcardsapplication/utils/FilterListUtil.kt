package com.example.discountcardsapplication.utils

import com.example.discountcardsapplication.models.Card
import com.example.discountcardsapplication.models.Company
import java.util.*

class FilterListUtil {
    companion object{
        fun filterList(text: String?, cardList: List<Card>?): ArrayList<Card> {
            val filteredCardList: ArrayList<Card> = ArrayList()
            if (cardList != null) {
                for(card in cardList){
                    if(card.companyName.lowercase(Locale.getDefault())
                            .contains(text!!.lowercase(Locale.getDefault()))){
                        filteredCardList.add(card)
                    }
                }
            }
            return filteredCardList
        }
        fun filteredCompaniesList(text: String?, companiesList: List<Company>?): ArrayList<Company>{
            val filteredCompaniesList: ArrayList<Company> = ArrayList<Company>()
            if(companiesList != null) {
                for(company in companiesList){
                    if(company.name.lowercase(Locale.getDefault())
                            .contains(text!!.lowercase(Locale.getDefault()))){
                        filteredCompaniesList.add(company)
                    }
                }
            }
            return filteredCompaniesList
        }
    }
}