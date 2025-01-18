package com.nahdlatululama.nahdlatutturot.data.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "favorite_kitab")
@Parcelize
data class KitabEntityFavorite(
    @ColumnInfo(name = "idkitab")
    @PrimaryKey
    val id: Int = -1,

    @ColumnInfo(name = "author")
    val author: String? = null,

    @ColumnInfo(name = "title")
    val title: String? = null,

    @ColumnInfo(name = "thumbnail_url")
    val thumbnailUrl: String? = null,

) : Parcelable
