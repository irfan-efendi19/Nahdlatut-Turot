package com.nahdlatululama.nahdlatutturot.ui.detail

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
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

        // Initialize ViewModel using ViewModelProvider
        val factory = ViewModelFactory.getInstance(this) // Assuming you have a ViewModelFactory
        detailViewModel = ViewModelProvider(this, factory)[DetailViewModel::class.java]

        // Set up the binding
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the BookList data passed from the HomeFragment
        val story = intent.getParcelableExtra<BookList>(DETAIL_STORY) as BookList

        // Set the book data in the ViewModel
        detailViewModel.setBookData(story)

        // Observe the LiveData for book data
        detailViewModel.bookData.observe(this, Observer { book ->
            // Update UI with the book details
            setupData(book)
        })

        // Handle favorite button click and state
        setFavorite(story)
    }


    private fun setupData(bookList: BookList) {
        // Load image with Glide
        Glide.with(applicationContext)
            .load(bookList.thumbnailUrl)
            .into(binding.ivCover)

        // Set text views with book details
        binding.tvTitle.text = bookList.title
        binding.tvDesc.text = bookList.description
        binding.tvAuthor.text = bookList.author

        // Handle PDF reading action
        binding.btnReadPdf.setOnClickListener {
            if (bookList.pdfUrl.isNullOrEmpty()) {
                Toast.makeText(this, "PDF tidak tersedia", Toast.LENGTH_SHORT).show()
            } else {
                ReadPdfActivity.start(this, bookList.pdfUrl)
            }
        }
    }

    private fun setFavorite(kitab: BookList) {
        // Pastikan kitab.id adalah Int
        val userEntity = KitabEntityFavorite(kitab.id, kitab.title, kitab.author)
        detailViewModel.getUserInfo(kitab.id).observe(this) { favorites ->
            isFavorite = favorites.isNotEmpty()
            binding.btnFav.imageTintList = if (favorites.isEmpty()) {
                ColorStateList.valueOf(Color.rgb(255, 255, 255)) // Not favorited (white color)
            } else {
                ColorStateList.valueOf(Color.rgb(255, 77, 77)) // Favorited (red color)
            }
        }

        // Set up the click listener for adding/removing from favorites
        binding.btnFav.setOnClickListener {
            if (isFavorite) {
                // Remove from favorites
                detailViewModel.deleteFromFavorite(userEntity).observe(this) { result ->
                    handleFavoriteResult(result)
                }
            } else {
                // Add to favorites
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
                // Show a loading indicator if needed
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
