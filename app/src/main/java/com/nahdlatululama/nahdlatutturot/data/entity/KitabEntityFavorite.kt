package com.nahdlatululama.nahdlatutturot.data.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "favorite_kitab")
@Parcelize
data class KitabEntityFavorite(
    @PrimaryKey
    val id: Int,
    val pages: String? = null,
    val publishedYear: String? = null,
    val author: String? = null,
    val genre: String? = null,
    val description: String? = null,
    val pdfUrl: String? = null,
    val title: String? = null,
    val thumbnailUrl: String? = null,
) : Parcelable

