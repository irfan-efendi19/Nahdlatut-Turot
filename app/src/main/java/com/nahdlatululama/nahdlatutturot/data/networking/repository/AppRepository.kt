package com.nahdlatululama.nahdlatutturot.data.networking.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.nahdlatululama.nahdlatutturot.data.networking.userPreference.UserModel
import com.nahdlatululama.nahdlatutturot.data.networking.userPreference.UserPreference
import com.google.gson.Gson
import com.nahdlatululama.nahdlatutturot.data.networking.response.LoginResponse
import com.nahdlatululama.nahdlatutturot.data.networking.response.RegisterResponse
import com.nahdlatululama.nahdlatutturot.data.networking.service.ApiService
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

class AppRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {

    fun register(
        name: String,
        email: String,
        password: String
    ): LiveData<ResultData<RegisterResponse>> = liveData {
        emit(ResultData.Loading)
        val response = apiService.register(name, email, password)
        if (!response.error) {
            emit(ResultData.Success(response))
        } else {
            emit(ResultData.Error(response.message))
        }
    }

    fun login(email: String, password: String): LiveData<ResultData<LoginResponse>> =
        liveData {
            emit(ResultData.Loading)
            try {
                val response = apiService.login(email, password)
                emit(ResultData.Success(response))
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, LoginResponse::class.java)
                emit(ResultData.Error(errorBody.message.toString()))
            }
        }

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logOut()
    }

    companion object {
        fun clearInstance() {
            instance = null
        }

        @Volatile
        private var instance: AppRepository? = null
        fun getInstance(
            service: ApiService,
            userPreference: UserPreference
        ): AppRepository =
            instance ?: synchronized(this) {
                instance ?: AppRepository(service, userPreference)
            }.also { instance = it }
    }
}
