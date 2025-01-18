package com.nahdlatululama.nahdlatutturot.ui.home.bottomnav.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nahdlatululama.nahdlatutturot.data.networking.repository.AppRepository
import com.nahdlatululama.nahdlatutturot.data.networking.repository.ResultData
import com.nahdlatululama.nahdlatutturot.data.networking.response.BookList
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: AppRepository) : ViewModel() {

    private val _searchResults = MutableLiveData<ResultData<List<BookList>>>()
    val searchResults: LiveData<ResultData<List<BookList>>> get() = _searchResults

    fun searchBooks(keyword: String) {
        _searchResults.value = ResultData.Loading

        // Call suspend function in a coroutine
        viewModelScope.launch {
            try {
                val result = repository.searchBooks(keyword)
                result.onSuccess { books ->
                    _searchResults.value = ResultData.Success(books)
                }.onFailure { error ->
                    _searchResults.value = ResultData.Error(error.message ?: "Terjadi kesalahan")
                }
            } catch (error: Exception) {
                _searchResults.value = ResultData.Error(error.message ?: "Terjadi kesalahan")
            }
        }
    }
}




