package com.nahdlatululama.nahdlatutturot.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.core.util.Pair
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nahdlatululama.nahdlatutturot.R
import com.nahdlatululama.nahdlatutturot.data.networking.response.BookList
import com.nahdlatululama.nahdlatutturot.databinding.ItemBookHomeBinding
import com.nahdlatululama.nahdlatutturot.ui.detail.DetailActivity


class KitabHomeAdapter : ListAdapter<BookList, KitabHomeAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBookHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val book = getItem(position)
        holder.bind(book)
    }

    inner class ViewHolder(private val binding: ItemBookHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(book: BookList) {
            with(binding) {
                tvTitle.text = book.title
                tvAuthor.text = book.author
                Glide.with(itemView.context)
                    .load(book.thumbnailUrl)
                    .placeholder(R.drawable.logoturot)
                    .into(ivCover)

                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.DETAIL_STORY, book)
                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            itemView.context as Activity,
                            Pair(ivCover, "cover"),
                            Pair(tvTitle, "title"),
                            Pair(tvAuthor, "author")
                        )
                    itemView.context.startActivity(intent, optionsCompat.toBundle())
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<BookList>() {
            override fun areItemsTheSame(
                oldItem: BookList,
                newItem: BookList
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: BookList,
                newItem: BookList
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}

