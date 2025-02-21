package com.nahdlatululama.nahdlatutturot.data.networking.response

import com.google.gson.annotations.SerializedName

data class UserResponse(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)
