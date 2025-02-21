package com.nahdlatululama.nahdlatutturot.data.networking.userPreference

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    suspend fun saveSession(user: UserModel) {
        Log.d("DEBUG_SESSION", "Saving session: userId=${user.userId}, token=${user.token}, isLogin=true")

        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = user.token
            preferences[NAME_KEY] = user.name
            preferences[USER_ID] = user.userId
            preferences[IS_LOGIN_KEY] = true
        }

        // Ambil ulang untuk memastikan data tersimpan
        dataStore.data.first().let { preferences ->
            Log.d(
                "DEBUG_SESSION",
                "Session stored: userId=${preferences[USER_ID]}, token=${preferences[TOKEN_KEY]}, isLogin=${preferences[IS_LOGIN_KEY]}"
            )
        }
    }


    fun getSession(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
                preferences[TOKEN_KEY] ?: "",
                preferences[NAME_KEY] ?: "",
                preferences[USER_ID] ?: "",
                preferences[IS_LOGIN_KEY] ?: false
            )
        }
    }


    suspend fun logOut() {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = ""
            preferences[NAME_KEY] = ""
            preferences[USER_ID] = ""
            preferences[IS_LOGIN_KEY] = false
            preferences.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null
        private val NAME_KEY = stringPreferencesKey("name")
        private val USER_ID = stringPreferencesKey("userId")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val IS_LOGIN_KEY = booleanPreferencesKey("isLogin")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}