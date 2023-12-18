package com.mshukshina.discountcardsapplication.domain.utils

import android.content.Intent
import com.mshukshina.discountcardsapplication.R
import com.mshukshina.discountcardsapplication.fragmentsandactivities.GeneratedCodeActivity
import com.mshukshina.discountcardsapplication.domain.models.Card
import com.mshukshina.discountcardsapplication.domain.models.GeneratedResult

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
