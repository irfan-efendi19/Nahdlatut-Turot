package com.nahdlatululama.nahdlatutturot.ui.home.bottomnav.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nahdlatululama.nahdlatutturot.data.networking.repository.AppRepository
import com.nahdlatululama.nahdlatutturot.data.networking.response.BookList
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: AppRepository) : ViewModel() {

    private val _searchResults = MutableLiveData<List<BookList>>()
    val searchResults: LiveData<List<BookList>> = _searchResults

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun searchBooks(keyword: String) {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            val result = repository.searchBooks(keyword)
            _isLoading.value = false
            result.fold(
                onSuccess = { books ->
                    _searchResults.value = books
                },
                onFailure = { error ->
                    _errorMessage.value = error.message
                }
            )
        }
    }
}
