package com.nahdlatululama.nahdlatutturot.ui.home.bottomnav.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nahdlatululama.nahdlatutturot.data.networking.repository.AppRepository
import com.nahdlatululama.nahdlatutturot.data.networking.repository.ResultData
import com.nahdlatululama.nahdlatutturot.data.networking.response.BookList

class HomeViewModel(val repository: AppRepository) : ViewModel() {

    private val _books = MutableLiveData<ResultData<List<BookList>>>()
    val books: LiveData<ResultData<List<BookList>>> get() = _books

    init {
        fetchBooks()
    }

    private fun fetchBooks() {
        repository.getBooks().observeForever {
            _books.value = it
        }
    }
}

