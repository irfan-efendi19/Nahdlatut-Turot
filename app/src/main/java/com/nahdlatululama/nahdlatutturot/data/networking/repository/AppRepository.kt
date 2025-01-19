package com.nahdlatululama.nahdlatutturot.data.networking.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.nahdlatululama.nahdlatutturot.data.entity.AppExecutors
import com.nahdlatululama.nahdlatutturot.data.entity.KitabEntityFavorite
import com.nahdlatululama.nahdlatutturot.data.networking.database.KitabDAO
import com.nahdlatululama.nahdlatutturot.data.networking.response.BookList
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
    private val userPreference: UserPreference,
    private val kitabDAO: KitabDAO,
    private val appExecutors: AppExecutors
) {


    private val result = MediatorLiveData<ResultData<List<KitabEntityFavorite>>>()
    private val resultS = MediatorLiveData<ResultData<String>>()

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

        apiService.getBook().enqueue(object : Callback<List<BookList>> {
            override fun onResponse(call: Call<List<BookList>>, response: Response<List<BookList>>) {
                if (response.isSuccessful) {
                    response.body()?.let { books ->
                        result.value = ResultData.Success(books)
                    } ?: run {
                        result.value = ResultData.Error("No data available")
                    }
                } else {
                    result.value = ResultData.Error("Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<BookList>>, t: Throwable) {
                result.value = ResultData.Error("Failure: ${t.localizedMessage ?: "Unknown error"}")
            }
        })
        return result
    }

    fun getBooksCategory(genre: String): LiveData<ResultData<List<BookList>>> {
        val result = MutableLiveData<ResultData<List<BookList>>>()
        result.value = ResultData.Loading

        apiService.getBooksByGenre(genre).enqueue(object : Callback<List<BookList>> {
            override fun onResponse(call: Call<List<BookList>>, response: Response<List<BookList>>) {
                if (response.isSuccessful) {
                    response.body()?.let { books ->
                        result.value = ResultData.Success(books)
                    } ?: run {
                        result.value = ResultData.Error("No data available")
                    }
                } else {
                    result.value = ResultData.Error("Error: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<List<BookList>>, t: Throwable) {
                result.value = ResultData.Error("Failure: ${t.localizedMessage ?: "Unknown error"}")
            }
        })

        return result
    }


    fun getFavoriteUser(): MediatorLiveData<ResultData<List<KitabEntityFavorite>>> {
        result.value = ResultData.Loading
        val localData = kitabDAO.getAllFavorites()
        result.addSource(localData) {
            result.value = ResultData.Success(it)
        }
        return result
    }

    fun getkitabInfo(id: Int): LiveData<List<KitabEntityFavorite>> {
        return kitabDAO.getFavoriteById(id)
    }

    fun insertFavorite(book: KitabEntityFavorite): LiveData<ResultData<String>> {
        resultS.value = ResultData.Loading

        appExecutors.diskIO.execute {
            try {
                kitabDAO.addToFavorites(book)
                resultS.postValue(ResultData.Success("${book.title} telah ditambahkan ke Favorite"))
            } catch (e: Exception) {
                resultS.postValue(ResultData.Error("Error saat menambahkan ke favorit"))
            }
        }
        return resultS
    }


    fun deleteFavorite(book: KitabEntityFavorite): MediatorLiveData<ResultData<String>> {
        resultS.value = ResultData.Loading
        appExecutors.diskIO.execute {
            kitabDAO.removeFromFavorites(book)
        }
        resultS.value = ResultData.Success("${book.title} telah dihapus dari Favorite")
        return resultS
    }

    suspend fun searchBooks(keyword: String): Result<List<BookList>> {
        return try {
            val response = apiService.searchBooks(keyword)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    companion object {
        fun clearInstance() {
            instance = null
        }

        @Volatile
        private var instance: AppRepository? = null
        fun getInstance(
            service: ApiService,
            userPreference: UserPreference,
            kitabDAO: KitabDAO,
            appExecutors: AppExecutors
        ): AppRepository =
            instance ?: synchronized(this) {
                instance ?: AppRepository(service, userPreference,kitabDAO,appExecutors)
            }.also { instance = it }
    }
}
