package com.nahdlatululama.nahdlatutturot.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.nahdlatululama.nahdlatutturot.R

class CardAdapter(private val images: List<Int>) : RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.card_holder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pager, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.imageView.setImageResource(images[position])
    }

    override fun getItemCount(): Int = images.size
}
