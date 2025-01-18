package com.nahdlatululama.nahdlatutturot.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nahdlatululama.nahdlatutturot.data.entity.KitabEntityFavorite
import com.nahdlatululama.nahdlatutturot.data.networking.repository.AppRepository
import com.nahdlatululama.nahdlatutturot.data.networking.repository.ResultData
import com.nahdlatululama.nahdlatutturot.data.networking.response.BookList

class DetailViewModel(private val appRepository: AppRepository) : ViewModel() {
    // LiveData for the book data
    private val _bookData = MutableLiveData<BookList>()
    val bookData: LiveData<BookList> get() = _bookData

    // Method to set the book data
    fun setBookData(book: BookList) {
        _bookData.value = book
    }

    fun getBookById(id: Int): LiveData<List<KitabEntityFavorite>> {
        return appRepository.getkitabInfo(id)
    }

    fun addToFavorite(userEntity: KitabEntityFavorite): LiveData<ResultData<String>> {
        return appRepository.insertFavorite(userEntity)
    }

    fun deleteFromFavorite(userEntity: KitabEntityFavorite): LiveData<ResultData<String>> {
        return appRepository.deleteFavorite(userEntity)
    }

    fun getUserInfo(id: Int): LiveData<List<KitabEntityFavorite>> {
        return appRepository.getkitabInfo(id)
    }
}