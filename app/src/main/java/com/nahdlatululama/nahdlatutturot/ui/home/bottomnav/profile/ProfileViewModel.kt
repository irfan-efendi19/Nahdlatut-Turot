package com.nahdlatululama.nahdlatutturot.ui.home.bottomnav.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nahdlatululama.nahdlatutturot.data.networking.repository.AppRepository
import com.nahdlatululama.nahdlatutturot.data.networking.response.UserResponse
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val repository: AppRepository,
) : ViewModel() {

    private val _userDetail = MutableLiveData<UserResponse?>()
    val userDetail: LiveData<UserResponse?> = _userDetail


    fun getUserDetail(userId: String, token: String) {
        Log.d("DEBUG_API", "Fetching user detail: userId=$userId, token=$token")

        viewModelScope.launch {
            try {
                repository.fetchUserDetail(userId, token) // ✅ Pastikan userId tidak null
                val userData = repository.userDetail.value
                if (userData != null) {
                    Log.d("DEBUG_API", "User detail retrieved: ${userData.name}")
                    _userDetail.postValue(userData)
                } else {
                    Log.e("DEBUG_API", "Error: userDetail is null after API call")
                }
            } catch (e: Exception) {
                Log.e("DEBUG_API", "Exception in getUserDetail(): ${e.message}")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            _userDetail.postValue(null)
        }
    }
}