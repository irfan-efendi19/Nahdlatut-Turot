package com.nahdlatululama.nahdlatutturot.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.nahdlatululama.nahdlatutturot.data.networking.repository.ResultData
import com.nahdlatululama.nahdlatutturot.data.networking.repository.AppRepository
import com.nahdlatululama.nahdlatutturot.data.networking.response.RegisterResponse

class SignUpViewModel (private var repository: AppRepository) : ViewModel(){
    suspend fun register(name: String, email: String, password: String): LiveData<ResultData<RegisterResponse>> {
        return repository.register(name, email, password)
    }
}