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

    private val _booksByCategory1 = MutableLiveData<ResultData<List<BookList>>>()
    val booksByCategory1: LiveData<ResultData<List<BookList>>> get() = _booksByCategory1

    private val _booksByCategory2 = MutableLiveData<ResultData<List<BookList>>>()
    val booksByCategory2: LiveData<ResultData<List<BookList>>> get() = _booksByCategory2

    private val _booksByCategory3 = MutableLiveData<ResultData<List<BookList>>>()
    val booksByCategory3: LiveData<ResultData<List<BookList>>> get() = _booksByCategory3

    init {
        fetchBooks()
        fetchBooksByCategory("Nahwu Sharaf")
        fetchBooksByCategory("Akidah")
        fetchBooksByCategory("Tasawuf")
    }

    private fun fetchBooks() {
        repository.getBooks().observeForever {
            _books.value = it
        }
    }

    private fun fetchBooksByCategory(category: String) {
        repository.getBooksCategory(category).observeForever {
            when (category) {
                "Nahwu Sharaf" -> _booksByCategory1.value = it
                "Akidah" -> _booksByCategory2.value = it
                "Tasawuf" -> _booksByCategory3.value = it
            }
        }
    }
    fun refreshData() {
        fetchBooks()
        fetchBooksByCategory("Nahwu Sharaf")
        fetchBooksByCategory("Akidah")
        fetchBooksByCategory("Tasawuf")
    }
}


