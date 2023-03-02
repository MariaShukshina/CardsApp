package com.example.discountcardsapplication.adapters

import android.content.res.Configuration
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.discountcardsapplication.R
import com.example.discountcardsapplication.databinding.CardItemBinding
import com.example.discountcardsapplication.fragmentsandactivities.MainActivity
import com.example.discountcardsapplication.domain.models.Card

class SavedCardsAdapter(private val activity: MainActivity) :
    RecyclerView.Adapter<SavedCardsAdapter.SavedCardsViewHolder>() {

    lateinit var onItemClickHandler: (Card) -> Unit
    lateinit var onFavIconClickHandler: (Card) -> Unit

    private val diffUtil = object : DiffUtil.ItemCallback<Card>() {
        override fun areItemsTheSame(oldItem: Card, newItem: Card): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Card, newItem: Card): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)

    class SavedCardsViewHolder(binding: CardItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val cardImage = binding.cardImage
        val favoriteIcon = binding.favoriteIcon
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedCardsViewHolder {
        val binding = CardItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        val cardWidth: Int
        val cardHeight: Int
        val orientation = activity.resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            cardWidth = parent.width - PORTRAIT_CARD_WIDTH_MARGIN
            cardHeight = cardWidth / 2
        } else {
            cardWidth = (parent.width - LANDSCAPE_CARD_WIDTH_MARGIN) / 2
            cardHeight = cardWidth / 2
        }
        val layoutParams = binding.cardImage.layoutParams
        layoutParams.width = cardWidth
        layoutParams.height = cardHeight

        return SavedCardsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SavedCardsViewHolder, position: Int) {
        val card = differ.currentList[position]
        Log.i("tag", card.imageResource.toString())

        if (card.isFavorite) {
            holder.favoriteIcon.setImageResource(R.drawable.ic_favorite_selected)
        } else {
            holder.favoriteIcon.setImageResource(R.drawable.ic_favorite)
        }

        if (card.customImage != null) {
            holder.cardImage.scaleType = ImageView.ScaleType.CENTER_CROP
            holder.cardImage.setImageURI(card.customImage!!.toUri())
        } else if (card.imageResource != null) {
            holder.cardImage.setImageResource(card.imageResource!!)
        } else {
            holder.cardImage.setImageResource(R.drawable.ic_placeholder)
        }

        holder.itemView.setOnClickListener {
            onItemClickHandler.invoke(card)
        }
        holder.favoriteIcon.setOnClickListener {
            onFavIconClickHandler.invoke(card)
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    companion object {
        private const val PORTRAIT_CARD_WIDTH_MARGIN = 4
        private const val LANDSCAPE_CARD_WIDTH_MARGIN = 2
    }
}
