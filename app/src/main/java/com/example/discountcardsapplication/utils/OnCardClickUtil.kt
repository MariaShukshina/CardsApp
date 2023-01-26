package com.example.discountcardsapplication.utils

import android.content.Intent
import com.example.discountcardsapplication.R
import com.example.discountcardsapplication.fragmentsandactivities.GeneratedCodeActivity
import com.example.discountcardsapplication.models.Card
import com.example.discountcardsapplication.models.GeneratedResult

object OnCardClickUtil {
    fun addCardDataToIntent(intent: Intent, codeResult: GeneratedResult, card: Card) {
        intent.putExtra(GeneratedCodeActivity.CODE_RESULT, codeResult)
        if (card.customImage != null) {
            intent.putExtra(GeneratedCodeActivity.CUSTOM_IMAGE, card.customImage)
        } else if (card.imageResource != null) {
            intent.putExtra(GeneratedCodeActivity.IMAGE_RESOURCE, card.imageResource)
        } else {
            intent.putExtra(GeneratedCodeActivity.DEFAULT_IMAGE, R.drawable.ic_placeholder)
        }
        intent.putExtra(GeneratedCodeActivity.COMPANY_NAME, card.companyName)
    }
}
