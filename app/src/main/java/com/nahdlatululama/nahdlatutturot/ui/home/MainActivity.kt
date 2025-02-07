package com.nahdlatululama.nahdlatutturot.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.nahdlatululama.nahdlatutturot.R
import com.nahdlatululama.nahdlatutturot.ViewModelFactory
import com.nahdlatululama.nahdlatutturot.databinding.ActivityMainBinding
import com.nahdlatululama.nahdlatutturot.ui.signin.SignInActivity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainActivityViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val navView: BottomNavigationView = binding.navView

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
}