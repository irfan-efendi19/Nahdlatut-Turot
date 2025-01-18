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
    // Mendapatkan buku favorit berdasarkan ID
    @Query("SELECT * FROM favorite_kitab WHERE idkitab = :id")
    fun getFavoriteById(id: Int): LiveData<List<KitabEntityFavorite>>

    // Mendapatkan semua buku favorit
    @Query("SELECT * FROM favorite_kitab")
    fun getAllFavorites(): LiveData<List<KitabEntityFavorite>>

    // Menambahkan buku ke favorit
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addToFavorites(book: KitabEntityFavorite)

    // Menghapus buku dari favorit
    @Delete
    fun removeFromFavorites(book: KitabEntityFavorite)

    @Update
    fun update(book: KitabEntityFavorite)

}
