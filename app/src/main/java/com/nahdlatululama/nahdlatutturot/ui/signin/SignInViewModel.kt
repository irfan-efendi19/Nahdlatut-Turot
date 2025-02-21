package com.nahdlatululama.nahdlatutturot.ui.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.nahdlatululama.nahdlatutturot.data.networking.repository.AppRepository
import com.nahdlatululama.nahdlatutturot.data.networking.repository.ResultData
import com.nahdlatululama.nahdlatutturot.data.networking.response.LoginResponse
import com.nahdlatululama.nahdlatutturot.data.networking.userPreference.UserModel

class SignInViewModel(private val repository: AppRepository) : ViewModel() {
    fun login(email: String, password: String) = repository.login(email, password)
    suspend fun saveSession(user: UserModel) {
        repository.saveSession(user)
    }
}