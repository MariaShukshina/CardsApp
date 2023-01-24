package com.example.discountcardsapplication.fragmentsandactivities

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.discountcardsapplication.adapters.SavedCardsAdapter
import com.example.discountcardsapplication.databinding.FragmentFavoritesBinding
import com.example.discountcardsapplication.models.Card
import com.example.discountcardsapplication.utils.CodeGenerator
import com.example.discountcardsapplication.utils.FilterListUtil
import com.example.discountcardsapplication.utils.OnCardClickUtil
import com.example.discountcardsapplication.viewmodels.MainActivityViewModel

class FavoritesFragment : Fragment() {
    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var savedCardsAdapter: SavedCardsAdapter
    private lateinit var mainActivityViewModel: MainActivityViewModel
    private lateinit var favSearchView: SearchView
    private var favoritesList = ArrayList<Card>()
    private var isShowingNoData = false
    private var searchText: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivityViewModel = (activity as MainActivity).viewModel
        savedCardsAdapter = SavedCardsAdapter(activity as MainActivity)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareCardsRecyclerView()
        savedCardsAdapter.onItemClickHandler = { onCardClick(it) }
        savedCardsAdapter.onFavIconClickHandler = { onFavIconClick(it) }
        observeAllCardsLiveData()

        favSearchView = binding.favCardsSearchView
        favSearchView.clearFocus()
        favSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchText = newText
                savedCardsAdapter.differ.submitList(filterFavorites(newText, favoritesList))
                return true
            }
        })
    }

    private fun filterFavorites(text: String?, list: List<Card>): List<Card> {
        if (text == null || text == "") {
            return list
        }
        val filteredList = FilterListUtil.filterList(text, list)
        if (filteredList.isEmpty() && !isShowingNoData) {
            isShowingNoData = true
            Toast.makeText(context, "No data found", Toast.LENGTH_SHORT).show()
        }
        if (filteredList.isNotEmpty()) {
            isShowingNoData = false
        }
        return filteredList
    }

    private fun prepareCardsRecyclerView() {
        val orientation = this.resources.configuration.orientation
        binding.rvFavCards.apply {
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                layoutManager = LinearLayoutManager(
                    context,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            } else {
                layoutManager = GridLayoutManager(
                    context,
                    2,
                    GridLayoutManager.VERTICAL,
                    false
                )
            }
            adapter = savedCardsAdapter
        }
    }

    private fun observeAllCardsLiveData() {
        mainActivityViewModel.getCards.observe(viewLifecycleOwner) {
            favoritesList = ArrayList()
            for (card in it) {
                if (card.isFavorite) {
                    favoritesList.add(card)
                }
            }
            savedCardsAdapter.differ.submitList(filterFavorites(searchText, favoritesList))
        }
    }

    private fun onFavIconClick(card: Card) {
        card.isFavorite = !card.isFavorite
        mainActivityViewModel.updateCard(card)
    }

    private fun onCardClick(card: Card) {
        val codeResult =
            CodeGenerator().generateQROrBarcodeImage(card.qrOrBarCode!!, card.barcodeFormat!!)
        if (codeResult.errorMessage != null) {
            Toast.makeText(context, codeResult.errorMessage, Toast.LENGTH_SHORT).show()
        } else {
            val intent = Intent(activity, GeneratedCodeActivity::class.java)
            OnCardClickUtil.onCardClick(intent, codeResult, card)
            startActivity(intent)
        }
    }
}
