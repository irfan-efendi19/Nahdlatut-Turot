package com.nahdlatululama.nahdlatutturot.ui.home.bottomnav.profile

import androidx.lifecycle.ViewModel
import com.nahdlatululama.nahdlatutturot.data.networking.repository.AppRepository

class ProfileViewModel(private val repository: AppRepository) : ViewModel() {
        suspend fun logout() {
            repository.logout()
        }
    }
