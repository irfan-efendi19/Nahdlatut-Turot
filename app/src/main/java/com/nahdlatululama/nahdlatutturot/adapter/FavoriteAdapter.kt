package com.nahdlatululama.nahdlatutturot.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nahdlatululama.nahdlatutturot.data.entity.KitabEntityFavorite
import com.nahdlatululama.nahdlatutturot.databinding.ItemPagerBinding
import com.nahdlatululama.nahdlatutturot.ui.detail.DetailActivity

class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.FavoriteHolder>() {

    // Define a ViewHolder class for binding each item
    class FavoriteHolder(val binding: ItemPagerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: KitabEntityFavorite) {
            binding.tvAuthor.text = data.author
            binding.tvTitle.text = data.title
            Glide.with(itemView.context).load(data.thumbnailUrl).into(binding.ivCover)
        }
    }

    // Create ViewHolder when needed
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteHolder {
        return FavoriteHolder(
            ItemPagerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    // Use DiffUtil for better performance when updating the list
    private val diffUtil = object : DiffUtil.ItemCallback<KitabEntityFavorite>() {
        override fun areItemsTheSame(
            oldItem: KitabEntityFavorite, newItem: KitabEntityFavorite
        ): Boolean {
            return oldItem.id == newItem.id // Assuming `id` is unique
        }

        override fun areContentsTheSame(
            oldItem: KitabEntityFavorite, newItem: KitabEntityFavorite
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)

    // Get item count from the current list in differ
    override fun getItemCount(): Int = differ.currentList.size

    // Bind the data and set up the click listener
    override fun onBindViewHolder(holder: FavoriteHolder, position: Int) {
        val item = differ.currentList[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
            intent.putExtra(DetailActivity.DETAIL_STORY, item)
            // Start the activity with the transition
            holder.itemView.context.startActivity(intent)
        }
    }

    // Declare a nullable onClick listener function (this can be used to handle clicks elsewhere if needed)
    var onClick: ((KitabEntityFavorite) -> Unit)? = null
}

