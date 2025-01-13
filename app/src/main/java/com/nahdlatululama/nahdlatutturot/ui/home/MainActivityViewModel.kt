package com.nahdlatululama.nahdlatutturot.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.nahdlatululama.nahdlatutturot.data.networking.userPreference.UserModel
import com.nahdlatululama.nahdlatutturot.data.networking.repository.AppRepository

class MainActivityViewModel(private val repository: AppRepository) : ViewModel() {

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
}