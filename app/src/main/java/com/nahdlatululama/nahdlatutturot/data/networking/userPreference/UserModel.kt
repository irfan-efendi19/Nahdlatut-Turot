package com.nahdlatululama.nahdlatutturot.data.networking.userPreference

data class UserModel(
    val token: String,
    val name: String,
    val userId: String,
    val isLogin: Boolean = false
)
