package com.nahdlatululama.nahdlatutturot.data.networking.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.nahdlatululama.nahdlatutturot.data.networking.response.BookList
import com.nahdlatululama.nahdlatutturot.data.networking.response.BookResponse
import com.nahdlatululama.nahdlatutturot.data.networking.response.LoginResponse
import com.nahdlatululama.nahdlatutturot.data.networking.response.RegisterResponse
import com.nahdlatululama.nahdlatutturot.data.networking.service.ApiService
import com.nahdlatululama.nahdlatutturot.data.networking.userPreference.UserModel
import com.nahdlatululama.nahdlatutturot.data.networking.userPreference.UserPreference
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

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

    fun getBooks(): LiveData<ResultData<List<BookList>>> {
        val result = MutableLiveData<ResultData<List<BookList>>>()
        result.value = ResultData.Loading

        apiService.getBook().enqueue(object : Callback<BookResponse> {
            override fun onResponse(call: Call<BookResponse>, response: Response<BookResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        result.value = ResultData.Success(it.listStory)
                    } ?: run {
                        result.value = ResultData.Error ("No data available")
                    }
                } else {
                    result.value = ResultData.Error("Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<BookResponse>, t: Throwable) {
                result.value = ResultData.Error("Failure: ${t.message}")
            }
        })
        return result
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
