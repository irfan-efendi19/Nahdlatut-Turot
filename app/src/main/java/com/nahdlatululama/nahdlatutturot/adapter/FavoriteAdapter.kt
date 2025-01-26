package com.nahdlatululama.nahdlatutturot.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nahdlatululama.nahdlatutturot.R
import com.nahdlatululama.nahdlatutturot.data.entity.KitabEntityFavorite
import com.nahdlatululama.nahdlatutturot.databinding.ItemPagerBinding
import com.nahdlatululama.nahdlatutturot.ui.detail.DetailActivity

class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.FavoriteHolder>() {

    // ViewHolder untuk mengelola item
    class FavoriteHolder(private val binding: ItemPagerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: KitabEntityFavorite) {
            binding.tvAuthor.text = data.author
            binding.tvTitle.text = data.title
            Glide.with(itemView.context)
                .load(data.thumbnailUrl)
                .placeholder(R.drawable.logoturot )
                .into(binding.ivCover)
        }
    }

    // DiffUtil untuk optimasi pembaruan data
    private val diffUtil = object : DiffUtil.ItemCallback<KitabEntityFavorite>() {
        override fun areItemsTheSame(
            oldItem: KitabEntityFavorite, newItem: KitabEntityFavorite
        ): Boolean {
            return oldItem.id == newItem.id // Asumsikan id unik
        }

        override fun areContentsTheSame(
            oldItem: KitabEntityFavorite, newItem: KitabEntityFavorite
        ): Boolean {
            return oldItem == newItem
        }
    }

    // AsyncListDiffer untuk mengelola daftar dengan performa tinggi
    val differ = AsyncListDiffer(this, diffUtil)
    val currentList: List<KitabEntityFavorite>
        get() = differ.currentList

    fun submitList(list: List<KitabEntityFavorite>) {
        differ.submitList(list)
    }

    var onClick: ((KitabEntityFavorite) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteHolder {
        val binding = ItemPagerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteHolder(binding)
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: FavoriteHolder, position: Int) {
        val item = differ.currentList[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailActivity::class.java).apply {
                putExtra(DetailActivity.DETAIL_FAVORITE, item)
            }
            holder.itemView.context.startActivity(intent)
        }

    }

}


