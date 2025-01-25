package com.nahdlatululama.nahdlatutturot.data.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "favorite_kitab")
@Parcelize
data class KitabEntityFavorite(
    @PrimaryKey val id: Int,
    val title: String,
    val author: String,
    val thumbnailUrl: String

) : Parcelable
