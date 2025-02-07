package com.nahdlatululama.nahdlatutturot.ui.detail

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setCustomView(R.layout.actionbar)

        // Mengambil data dari Intent
        val story = intent.getParcelableExtra<BookList>(DETAIL_STORY)
        val favorite = intent.getParcelableExtra<KitabEntityFavorite>(DETAIL_FAVORITE)

        if (story != null) {
            setupBookDetail(story)
        } else if (favorite != null) {
            setupFavoriteDetail(favorite)
        } else {
            Toast.makeText(this, "Data tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun setupBookDetail(book: BookList) {
        detailViewModel.setBookData(book)
        detailViewModel.bookData.observe(this) { bookData ->
            setupData(bookData)
        }
        setFavorite(book)
    }

    private fun setupFavoriteDetail(favorite: KitabEntityFavorite) {
        val book = BookList(
            id = favorite.id,
            title = favorite.title,
            author = favorite.author,
            description = favorite.description,
            thumbnailUrl = favorite.thumbnailUrl,
            pdfUrl = favorite.pdfUrl,
            pages = favorite.pages,
            publishedYear = favorite.publishedYear,
            genre = favorite.genre,
        )
        setupData(book)
        setFavorite(book)
    }

    private fun setupData(bookList: BookList) {
        Glide.with(this)
            .load(bookList.thumbnailUrl)
            .placeholder(R.drawable.logoturot)
            .into(binding.ivCover)

        supportActionBar?.title = bookList.title

        binding.tvTitle.text = bookList.title
        binding.tvDesc.text = bookList.description
        binding.tvAuthor.text = bookList.author
        binding.tvJumblahHalaman.text = bookList.pages
        binding.tvTahunterbit.text = bookList.publishedYear ?: "-"
        binding.tvKategori.text = bookList.genre ?: "-"

        binding.btnReadPdf.apply {
            visibility = if (bookList.pdfUrl.isNullOrEmpty() || bookList.pdfUrl == "") View.GONE else View.VISIBLE
            setOnClickListener {
                bookList.pdfUrl?.let {
                    ReadPdfActivity.start(context, it)
                }
            }
        }
    }

    private fun setFavorite(book: BookList) {
        val userEntity = KitabEntityFavorite(
            id = book.id,
            title = book.title,
            author = book.author,
            thumbnailUrl = book.thumbnailUrl,
            description = book.description,
            pdfUrl = book.pdfUrl,
            pages = book.pages,
            genre = book.genre,
            publishedYear = book.publishedYear
        )

        detailViewModel.getUserInfo(book.id).observe(this) { favorites ->
            isFavorite = favorites.isNotEmpty()
            binding.btnFav.imageTintList = if (isFavorite) {
                ColorStateList.valueOf(Color.rgb(255, 77, 77)) // Favorited (red color)
            } else {
                ContextCompat.getColorStateList(this, R.color.black)  // Not favorited (white color)
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
            is ResultData.Loading -> binding.btnFav.isEnabled = false
            is ResultData.Success -> {
                binding.btnFav.isEnabled = true
                Toast.makeText(this, result.data, Toast.LENGTH_SHORT).show()
            }
            is ResultData.Error -> {
                binding.btnFav.isEnabled = true
                Toast.makeText(this, "Terjadi kesalahan: ${result.error}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        finish()
        return true
    }

    companion object {
        const val DETAIL_STORY = "detail_story"
        const val DETAIL_FAVORITE = "detail_favorite"
    }
}
