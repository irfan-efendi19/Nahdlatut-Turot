package com.nahdlatululama.nahdlatutturot

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nahdlatululama.nahdlatutturot.ui.signin.SignInViewModel
import com.nahdlatululama.nahdlatutturot.ui.signup.SignUpViewModel
import com.nahdlatululama.nahdlatutturot.data.di.Injection
import com.nahdlatululama.nahdlatutturot.data.networking.repository.AppRepository
import com.nahdlatululama.nahdlatutturot.ui.home.MainActivityViewModel

class ViewModelFactory(private val repository: AppRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SignUpViewModel::class.java) -> {
                SignUpViewModel(repository) as T
            }

            modelClass.isAssignableFrom(SignInViewModel::class.java) -> {
                SignInViewModel(repository) as T
            }

            modelClass.isAssignableFrom(MainActivityViewModel::class.java) -> {
                MainActivityViewModel(repository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun clearInstance() {
            AppRepository.clearInstance()
            INSTANCE = null
        }

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(Injection.provideRepository(context))
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}