package com.nahdlatululama.nahdlatutturot.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.nahdlatululama.nahdlatutturot.R
import com.nahdlatululama.nahdlatutturot.ViewModelFactory
import com.nahdlatululama.nahdlatutturot.databinding.ActivityMainBinding
import com.nahdlatululama.nahdlatutturot.ui.signin.SignInActivity
import kotlinx.coroutines.launch
import java.io.File

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainActivityViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val navView: BottomNavigationView = binding.navView

        auth = FirebaseAuth.getInstance()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController

        navView.setupWithNavController(navController)
        getSession()

    }

    private fun getSession() {
        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {

                val intent = Intent(this, SignInActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()

            } else {
                lifecycleScope.launch {
//                    viewModel.getStory.observe(this@HomeActivity) { result ->
//                        adapter.submitData(lifecycle, result)
//                        showLoading(false)
//                    }
//                    showLoading(false)
                }
            }
        }
    }

    //clear cache
    override fun onDestroy() {
        super.onDestroy()
        clearCache(this)
    }

    private fun clearCache(context: Context) {
        try {
            val dir: File = context.cacheDir
            deleteDir(dir)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun deleteDir(dir: File?): Boolean {
        if (dir != null && dir.isDirectory) {
            for (child in dir.list() ?: arrayOf()) {
                val success = deleteDir(File(dir, child))
                if (!success) return false
            }
            return dir.delete()
        } else if (dir != null && dir.isFile) {
            return dir.delete()
        }
        return false
    }
}