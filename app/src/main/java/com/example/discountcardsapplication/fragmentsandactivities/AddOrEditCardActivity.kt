package com.example.discountcardsapplication.fragmentsandactivities

import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.discountcardsapplication.R
import com.example.discountcardsapplication.databinding.ActivityAddOrEditCardBinding
import com.example.discountcardsapplication.fragmentsandactivities.ChooseCompanyActivity.Companion.COMPANY_IMAGE
import com.example.discountcardsapplication.fragmentsandactivities.ChooseCompanyActivity.Companion.COMPANY_NAME

class AddOrEditCardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddOrEditCardBinding
    private var name: String = ""
    private var image: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddOrEditCardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonCancel.setOnClickListener {
            finish()
        }
        getDataFromIntentAndSetInViews()
    }

    private fun getDataFromIntentAndSetInViews(){
        val intent = intent
        if(intent.hasExtra(COMPANY_NAME) && intent.hasExtra(COMPANY_IMAGE)) {
            name = intent.getStringExtra(COMPANY_NAME)!!
            image = intent.getIntExtra(COMPANY_IMAGE, 0)

            binding.newCardImage.setImageResource(image)
            binding.etCompanyName.setText(name)
        }
    }
}