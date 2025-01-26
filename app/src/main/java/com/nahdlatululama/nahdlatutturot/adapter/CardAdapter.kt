package com.nahdlatululama.nahdlatutturot.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.nahdlatululama.nahdlatutturot.R
import com.nahdlatululama.nahdlatutturot.data.entity.BannerEntity

class CardAdapter(private val images: List<BannerEntity>) : RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    class CardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.card_holder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_card_book, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val item = images[position]
        holder.imageView.setImageResource(item.imageRes)

        // Handle click event to open link
        holder.imageView.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.url))
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = images.size
}
