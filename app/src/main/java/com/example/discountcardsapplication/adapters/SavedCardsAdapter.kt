package com.example.discountcardsapplication.adapters

import android.content.res.Configuration
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.discountcardsapplication.R
import com.example.discountcardsapplication.databinding.CardItemBinding
import com.example.discountcardsapplication.fragmentsandactivities.MainActivity
import com.example.discountcardsapplication.models.Card


class SavedCardsAdapter(private val activity: MainActivity): RecyclerView.Adapter<SavedCardsAdapter.SavedCardsViewHolder>() {
    private var cardsList = listOf<Card>()

    //lateinit var onItemClick: (Company) -> Unit

    fun setSavedCardsList(cardsList: List<Card>){
        this.cardsList = cardsList
        notifyDataSetChanged()
    }

    class SavedCardsViewHolder(binding: CardItemBinding): RecyclerView.ViewHolder(binding.root){
        val cardImage = binding.cardImage
        val favoriteIcon = binding.favoriteIcon
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedCardsViewHolder {
        val binding = CardItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false)

        val cardWidth: Int
        val cardHeight: Int
        val orientation = activity.resources.configuration.orientation
        if(orientation == Configuration.ORIENTATION_PORTRAIT ){
            cardWidth = parent.width - 4
            cardHeight =  cardWidth / 2
        } else {
            cardWidth = (parent.width - 2) / 2
            cardHeight =  cardWidth / 2
        }
        val layoutParams = binding.cardImage.layoutParams
        layoutParams.width = cardWidth
        layoutParams.height = cardHeight

        return SavedCardsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SavedCardsViewHolder, position: Int) {
        Log.i("tag", cardsList[position].imageResource.toString())

        if(cardsList[position].customImage != null){
            holder.cardImage.setImageURI(cardsList[position].customImage!!.toUri())

        } else if(cardsList[position].imageResource != null){
            holder.cardImage.setImageResource(cardsList[position].imageResource!!)
        } else {
            holder.cardImage.setImageResource(R.drawable.ic_placeholder)
        }

        holder.itemView.setOnClickListener {
            //onItemClick.invoke(companiesList[position])
        }
    }

    override fun getItemCount(): Int {
        return cardsList.size
    }
}