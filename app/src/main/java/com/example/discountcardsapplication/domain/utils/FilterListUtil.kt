package com.example.discountcardsapplication.domain.utils

import com.example.discountcardsapplication.domain.models.Card
import com.example.discountcardsapplication.domain.models.Company
import java.util.*

object FilterListUtil {
    fun filterList(text: String?, cardList: List<Card>?): List<Card> {
        val filteredCardList: ArrayList<Card> = ArrayList()
        if (cardList != null) {
            for (card in cardList) {
                if (card.companyName.lowercase(Locale.getDefault())
                    .contains(text!!.lowercase(Locale.getDefault()))
                ) {
                    filteredCardList.add(card)
                }
            }
        }
        return filteredCardList
    }

    fun filteredCompaniesList(text: String?, companiesList: List<Company>?): ArrayList<Company> {
        val filteredCompaniesList: ArrayList<Company> = ArrayList<Company>()
        if (companiesList != null) {
            for (company in companiesList) {
                if (company.name.lowercase(Locale.getDefault())
                    .contains(text!!.lowercase(Locale.getDefault()))
                ) {
                    filteredCompaniesList.add(company)
                }
            }
        }
        return filteredCompaniesList
    }
}
