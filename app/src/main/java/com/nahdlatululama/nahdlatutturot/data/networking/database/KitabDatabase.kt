package com.nahdlatululama.nahdlatutturot.data.networking.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nahdlatululama.nahdlatutturot.data.entity.KitabEntityFavorite

@Database(entities = [KitabEntityFavorite::class], version = 11, exportSchema = false)
abstract class KitabDatabase : RoomDatabase() {
    abstract fun favoriteKitabDao(): KitabDAO

    companion object {
        @Volatile
        private var INSTANCE: KitabDatabase? = null

        fun getInstance(context: Context): KitabDatabase {
            if (INSTANCE == null) {
                synchronized(KitabDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        KitabDatabase::class.java, "fav_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE as KitabDatabase
        }
    }
}
