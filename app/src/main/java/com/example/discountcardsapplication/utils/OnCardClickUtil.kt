package com.example.discountcardsapplication.utils

import android.content.Intent
import com.example.discountcardsapplication.R
import com.example.discountcardsapplication.fragmentsandactivities.HomeFragment
import com.example.discountcardsapplication.models.Card
import com.example.discountcardsapplication.models.GeneratedResult

object OnCardClickUtil {
    fun onCardClick(intent: Intent, codeResult: GeneratedResult, it: Card) {
        intent.putExtra(HomeFragment.CODE_RESULT, codeResult)
        if (it.customImage != null) {
            intent.putExtra(HomeFragment.CUSTOM_IMAGE, it.customImage)
        } else if (it.imageResource != null) {
            intent.putExtra(HomeFragment.IMAGE_RESOURCE, it.imageResource)
        } else {
            intent.putExtra(HomeFragment.DEFAULT_IMAGE, R.drawable.ic_placeholder)
        }
        intent.putExtra(HomeFragment.COMPANY_NAME, it.companyName)
    }
}
