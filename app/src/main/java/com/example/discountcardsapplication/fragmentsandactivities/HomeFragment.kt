package com.example.discountcardsapplication.fragmentsandactivities

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.discountcardsapplication.R
import com.example.discountcardsapplication.adapters.SavedCardsAdapter
import com.example.discountcardsapplication.databinding.FragmentHomeBinding
import com.example.discountcardsapplication.utils.CodeGenerator
import com.example.discountcardsapplication.viewmodels.MainActivityViewModel

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var savedCardsAdapter: SavedCardsAdapter
    private lateinit var mainActivityViewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivityViewModel = (activity as MainActivity).viewModel
        savedCardsAdapter = SavedCardsAdapter(activity as MainActivity)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareCardsRecyclerView()
        observeAllCardsLiveData()

        onFabAddCardClick()
        onCardClick()
    }

    private fun onCardClick() {
        savedCardsAdapter.onItemClick = {
            val codeResult = CodeGenerator().generateQROrBarcodeImage(it.qrOrBarCode!!, it.barcodeFormat!!)
            if (codeResult.errorMessage != null){
                Toast.makeText(context, codeResult.errorMessage, Toast.LENGTH_SHORT).show()
            }else{
                val intent = Intent(activity, GeneratedCodeActivity::class.java)
                intent.putExtra(CODE_RESULT, codeResult)
                if(it.customImage != null) {
                    intent.putExtra(CUSTOM_IMAGE, it.customImage)
                } else if (it.imageResource != null) {
                    intent.putExtra(IMAGE_RESOURCE, it.imageResource)
                } else {
                    intent.putExtra(DEFAULT_IMAGE, R.drawable.ic_placeholder)
                }
                intent.putExtra(COMPANY_NAME, it.companyName)
                startActivity(intent)
            }
        }
    }

    private fun onFabAddCardClick() {
        binding.fabAddCard.setOnClickListener {
            startActivity(Intent(activity, ChooseCompanyActivity::class.java))
        }
    }

    private fun prepareCardsRecyclerView() {
        val orientation = this.resources.configuration.orientation
        binding.rvSavedCardsList.apply {
            if(orientation == Configuration.ORIENTATION_PORTRAIT){
                layoutManager = LinearLayoutManager(context,
                    LinearLayoutManager.VERTICAL, false)
            } else {
                layoutManager = GridLayoutManager(context, 2,
                    GridLayoutManager.VERTICAL, false)
            }
            adapter = savedCardsAdapter
        }
    }

    private fun observeAllCardsLiveData(){
        mainActivityViewModel.getCards.observe(viewLifecycleOwner){
            savedCardsAdapter.setSavedCardsList(it)
        }
    }
    companion object {
        const val CODE_RESULT = "CODE_RESULT"
        const val CUSTOM_IMAGE = "CUSTOM_IMAGE"
        const val IMAGE_RESOURCE = "IMAGE_RESOURCE"
        const val DEFAULT_IMAGE = "DEFAULT_IMAGE"
        const val COMPANY_NAME = "COMPANY_NAME"
    }
}