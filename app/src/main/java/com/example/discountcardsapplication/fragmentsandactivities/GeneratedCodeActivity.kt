package com.example.discountcardsapplication.fragmentsandactivities

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.example.discountcardsapplication.R
import com.example.discountcardsapplication.databinding.ActivityGeneratedCodeBinding
import com.example.discountcardsapplication.fragmentsandactivities.HomeFragment.Companion.CODE_RESULT
import com.example.discountcardsapplication.fragmentsandactivities.HomeFragment.Companion.COMPANY_NAME
import com.example.discountcardsapplication.fragmentsandactivities.HomeFragment.Companion.CUSTOM_IMAGE
import com.example.discountcardsapplication.fragmentsandactivities.HomeFragment.Companion.DEFAULT_IMAGE
import com.example.discountcardsapplication.fragmentsandactivities.HomeFragment.Companion.IMAGE_RESOURCE
import com.example.discountcardsapplication.models.GeneratedResult

class GeneratedCodeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGeneratedCodeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGeneratedCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSupportActionBar()
        getGeneratedDataFromIntentAndSetInViews()
    }

    private fun getGeneratedDataFromIntentAndSetInViews() {
        val intent = intent
        if (intent.hasExtra(CODE_RESULT)) {
            val codeResult = intent.getParcelableExtra<GeneratedResult>(CODE_RESULT)
            binding.generatedImage.setImageBitmap(codeResult!!.bitmap)
            binding.tvScannedCode.text = codeResult.qrCode
        }
        if (intent.hasExtra(CUSTOM_IMAGE)) {
            val customImage = intent.getStringExtra(CUSTOM_IMAGE)!!.toUri()
            binding.savedCardImage.scaleType = ImageView.ScaleType.CENTER_CROP
            binding.savedCardImage.setImageURI(customImage)
        } else if (intent.hasExtra(IMAGE_RESOURCE)) {
            binding.savedCardImage.setImageResource(intent.getIntExtra(IMAGE_RESOURCE, 0))
        } else if (intent.hasExtra(DEFAULT_IMAGE)) {
            binding.savedCardImage.setImageResource(R.drawable.ic_placeholder)
        }
        if (intent.hasExtra(COMPANY_NAME)) {
            supportActionBar?.title = intent.getStringExtra(COMPANY_NAME)
        }
    }

    private fun setupSupportActionBar() {
        setSupportActionBar(binding.savedCardToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.savedCardToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}
