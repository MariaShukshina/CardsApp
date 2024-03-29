package com.mshukshina.discountcardsapplication.fragmentsandactivities

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import com.mshukshina.discountcardsapplication.R
import com.mshukshina.discountcardsapplication.adapters.SavedCardsAdapter
import com.mshukshina.discountcardsapplication.databinding.FragmentFavoritesBinding
import com.mshukshina.discountcardsapplication.domain.models.Card
import com.mshukshina.discountcardsapplication.domain.utils.CodeGenerator
import com.mshukshina.discountcardsapplication.domain.utils.FilterListUtil
import com.mshukshina.discountcardsapplication.domain.utils.OnCardClickUtil
import com.mshukshina.discountcardsapplication.viewmodels.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment() {
    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var savedCardsAdapter: SavedCardsAdapter
    private lateinit var favSearchView: SearchView
    private var favoritesList = ArrayList<Card>()
    private var isShowingData = true
    private var searchText: String? = null

    private val mainActivityViewModel by viewModels<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedCardsAdapter = SavedCardsAdapter(activity as MainActivity)
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.fade)
        exitTransition = inflater.inflateTransition(R.transition.fade)
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
        if (filteredList.isEmpty() && isShowingData) {
            isShowingData = false
            Toast.makeText(context, "No data found", Toast.LENGTH_SHORT).show()
        }
        if (filteredList.isNotEmpty()) {
            isShowingData = true
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
            OnCardClickUtil.addCardDataToIntent(intent, codeResult, card)
            startActivity(intent)
        }
    }
}
