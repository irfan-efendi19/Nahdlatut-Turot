package com.nahdlatululama.nahdlatutturot.ui.home.bottomnav.favorite

import androidx.lifecycle.ViewModel
import com.nahdlatululama.nahdlatutturot.data.networking.repository.AppRepository

class FavoriteViewModel(private val userRepository: AppRepository): ViewModel() {

    fun getUserFavorite() = userRepository.getFavoriteUser()
}