package com.nahdlatululama.nahdlatutturot.data.networking.service


import com.nahdlatululama.nahdlatutturot.data.networking.response.BookList
import com.nahdlatululama.nahdlatutturot.data.networking.response.LoginResponse
import com.nahdlatululama.nahdlatutturot.data.networking.response.RegisterResponse
import com.nahdlatululama.nahdlatutturot.data.networking.response.UserResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {
    @FormUrlEncoded
    @POST("auth/register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("auth/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): LoginResponse

    @GET("books")
    fun getBook(): Call<List<BookList>>

    @GET("books")
    fun getBooksByGenre(@Query("genre") genre: String): Call<List<BookList>>

    @GET("books/search")
    suspend fun searchBooks(
        @Query("q") keyword: String
    ): Response<List<BookList>>

    @GET("user/{id}")
    suspend fun getUserDetail(
        @Header("Authorization") authHeader: String,
        @Path("id") userId: String
    ): Response<UserResponse>
}