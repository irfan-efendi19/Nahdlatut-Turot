package com.nahdlatululama.nahdlatutturot.ui.detail

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.nahdlatululama.nahdlatutturot.R
import com.nahdlatululama.nahdlatutturot.ViewModelFactory
import com.nahdlatululama.nahdlatutturot.data.entity.KitabEntityFavorite
import com.nahdlatululama.nahdlatutturot.data.networking.repository.ResultData
import com.nahdlatululama.nahdlatutturot.data.networking.response.BookList
import com.nahdlatululama.nahdlatutturot.databinding.ActivityDetailBinding
import com.nahdlatululama.nahdlatutturot.ui.read.ReadPdfActivity

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel
    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val factory = ViewModelFactory.getInstance(this)
        detailViewModel = ViewModelProvider(this, factory)[DetailViewModel::class.java]

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val story = intent.getParcelableExtra<BookList>(DETAIL_STORY) as BookList

        detailViewModel.setBookData(story)

        detailViewModel.bookData.observe(this, Observer { book ->
            setupData(book)
        })

        setFavorite(story)
    }


    private fun setupData(bookList: BookList) {
        Glide.with(applicationContext)
            .load(bookList.thumbnailUrl)
            .placeholder(R.drawable.logoturot)
            .into(binding.ivCover)

        binding.tvTitle.text = bookList.title
        binding.tvDesc.text = bookList.description
        binding.tvAuthor.text = bookList.author

//        binding.btnReadPdf.setOnClickListener {
//            if (bookList.pdfUrl.isNullOrEmpty()) {
//                Toast.makeText(this, "PDF tidak tersedia", Toast.LENGTH_SHORT).show()
//            } else {
//                ReadPdfActivity.start(this, bookList.pdfUrl)
//            }
//        }

        binding.btnReadPdf.apply {
            visibility = if (bookList.pdfUrl.isNullOrEmpty()) {
                View.GONE
            } else {
                View.VISIBLE
            }

            setOnClickListener {
                ReadPdfActivity.start(context, bookList.pdfUrl.toString())
            }
        }

    }

    private fun setFavorite(kitab: BookList) {
        val userEntity = KitabEntityFavorite(kitab.id, kitab.title, kitab.author)
        detailViewModel.getUserInfo(kitab.id).observe(this) { favorites ->
            isFavorite = favorites.isNotEmpty()
            binding.btnFav.imageTintList = if (favorites.isEmpty()) {
                ColorStateList.valueOf(Color.rgb(255, 255, 255)) // Not favorited (white color)
            } else {
                ColorStateList.valueOf(Color.rgb(255, 77, 77)) // Favorited (red color)
            }
        }

        binding.btnFav.setOnClickListener {
            if (isFavorite) {
                detailViewModel.deleteFromFavorite(userEntity).observe(this) { result ->
                    handleFavoriteResult(result)
                }
            } else {
                detailViewModel.addToFavorite(userEntity).observe(this) { result ->
                    handleFavoriteResult(result)
                }
            }
        }
    }


    private fun handleFavoriteResult(result: ResultData<String>) {
        when (result) {
            is ResultData.Loading -> {
                binding.btnFav.hide()
            }
            is ResultData.Success -> {
                binding.btnFav.show()
                Toast.makeText(this, result.data, Toast.LENGTH_SHORT).show()
            }
            is ResultData.Error -> {
                binding.btnFav.show()
                Toast.makeText(this, "Opps, ada yang salah nih.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        const val DETAIL_STORY = "detail_story"
    }
}
