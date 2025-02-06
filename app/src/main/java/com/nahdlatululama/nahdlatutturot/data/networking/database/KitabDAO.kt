package com.nahdlatululama.nahdlatutturot.data.networking.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.nahdlatululama.nahdlatutturot.data.entity.KitabEntityFavorite

@Dao
interface KitabDAO {
    @Query("SELECT * FROM favorite_kitab WHERE id = :id")
    fun getFavoriteById(id: Int): LiveData<List<KitabEntityFavorite>>

    @Query("SELECT * FROM favorite_kitab")
    fun getAllFavorites(): LiveData<List<KitabEntityFavorite>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addToFavorites(book: KitabEntityFavorite)

    @Delete
    fun removeFromFavorites(book: KitabEntityFavorite)

    @Update
    fun update(book: KitabEntityFavorite)
}
