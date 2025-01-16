package com.nahdlatululama.nahdlatutturot.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.nahdlatululama.nahdlatutturot.data.networking.response.BookList
import com.nahdlatululama.nahdlatutturot.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {


    private var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val story = intent.getParcelableExtra<BookList>(DETAIL_STORY) as BookList
        setupData(story)
    }

    private fun setupData(bookList: BookList) {
        Glide.with(applicationContext)
            .load(bookList.thumbnailUrl)
            .into(binding.ivCover)
        binding.tvTitle.text = bookList.title
        binding.tvDesc.text = bookList.description
    }


    companion object {
        const val DETAIL_STORY = "detail_story"
    }
}