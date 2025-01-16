package com.nahdlatululama.nahdlatutturot.data.networking.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


data class BookResponse(
	@SerializedName("books")
	val listStory: List<BookList> = emptyList(),
)


@Parcelize
data class BookList(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("pages")
	val pages: String? = null,

	@field:SerializedName("published_year")
	val publishedYear: String? = null,

	@field:SerializedName("author")
	val author: String? = null,

	@field:SerializedName("genre")
	val genre: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("pdf_url")
	val pdfUrl: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("thumbnail_url")
	val thumbnailUrl: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
): Parcelable
