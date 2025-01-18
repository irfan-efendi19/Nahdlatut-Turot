package com.nahdlatululama.nahdlatutturot.data.di

import android.content.Context
import com.nahdlatululama.nahdlatutturot.data.entity.AppExecutors
import com.nahdlatululama.nahdlatutturot.data.networking.database.KitabDatabase
import com.nahdlatululama.nahdlatutturot.data.networking.repository.AppRepository
import com.nahdlatululama.nahdlatutturot.data.networking.service.ApiConfig
import com.nahdlatululama.nahdlatutturot.data.networking.userPreference.UserPreference
import com.nahdlatululama.nahdlatutturot.data.networking.userPreference.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): AppRepository = runBlocking{
        val pref = UserPreference.getInstance(context.dataStore)
        val user = pref.getSession().first()
        val apiService = ApiConfig.getApiService(user.token)
        val database = KitabDatabase.getInstance(context)
        val dao = database.favoriteKitabDao()
        val appExecutors = AppExecutors()
        AppRepository.getInstance(apiService, pref,dao,appExecutors)
    }
}