package com.nahdlatululama.nahdlatutturot.ui.signin

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.nahdlatululama.nahdlatutturot.R
import com.nahdlatululama.nahdlatutturot.ViewModelFactory
import com.nahdlatululama.nahdlatutturot.data.networking.repository.ResultData
import com.nahdlatululama.nahdlatutturot.data.networking.userPreference.UserModel
import com.nahdlatululama.nahdlatutturot.databinding.ActivitySigninBinding
import com.nahdlatululama.nahdlatutturot.ui.home.MainActivity
import com.nahdlatululama.nahdlatutturot.ui.signup.SignUpActivity
import com.nahdlatululama.nahdlatutturot.utill.isNetworkConnected
import com.nahdlatululama.nahdlatutturot.utill.showToast
import kotlinx.coroutines.launch

class SignInActivity : AppCompatActivity() {

    private lateinit var context: Context
    private val viewModel by viewModels<SignInViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private var _binding: ActivitySigninBinding? = null
    private val binding get() = _binding!!

    // Google Sign-In
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth
    private val RC_SIGN_IN = 123 // Request code untuk Google Sign-In

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        context = this@SignInActivity

        FirebaseApp.initializeApp(this)

        // Inisialisasi Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()

        // Konfigurasi Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.your_web_client_id)) // Ganti dengan Client ID Anda
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        supportActionBar?.hide()
        showLoading(false)
        setupAction()
        setupView()

        // Tombol Sign Up
        binding.signupButton.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        // Tombol Login dengan Google
        binding.googleSignInButton.setOnClickListener {
            signInWithGoogle()
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        // Tombol Login dengan Email/Password
        binding.signinButton.setOnClickListener {
            if (isNetworkConnected(context)) {
                val email = binding.emailEditText.text.toString()
                val password = binding.passwordEditText.text.toString()

                if (email.isEmpty()) {
                    binding.emailEditText.error = "Email Tidak Boleh Kosong"
                } else if (password.isEmpty()) {
                    binding.passwordEditText.error = "Password Tidak Boleh Kosong"
                } else {
                    loginWithEmail(email, password)
                }
            } else {
                showToast(context, "Tidak Ada Internet")
            }
        }
    }

    private fun loginWithEmail(email: String, password: String) {
        lifecycleScope.launch {
            viewModel.login(email, password).observe(this@SignInActivity) { result ->
                when (result) {
                    is ResultData.Loading -> showLoading(true)
                    is ResultData.Success -> {
                        showLoading(false)
                        showToast(this@SignInActivity, "Berhasil Masuk!")
                        save(
                            UserModel(
                                result.data.loginResult?.token.toString(),
                                result.data.loginResult?.name.toString(),
                                result.data.loginResult?.userId.toString(),
                                true
                            )
                        )
                    }
                    is ResultData.Error -> {
                        showLoading(false)
                        showToast(this@SignInActivity, result.error)
                    }
                }
            }
        }
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Handle hasil dari Google Sign-In
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleGoogleSignInResult(task)
        }
    }

    private fun handleGoogleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            if (account != null) {
                firebaseAuthWithGoogle(account.idToken!!)
            }
        } catch (e: ApiException) {
            showToast(context, "Google Sign-In Gagal: ${e.message}")
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Login berhasil
                    showToast(context, "Berhasil Masuk dengan Google!")
                    save(
                        UserModel(
                            token = firebaseAuth.currentUser?.uid ?: "",
                            name = firebaseAuth.currentUser?.displayName ?: "",
                            userId = firebaseAuth.currentUser?.uid ?: "",
                            isLogin = true
                        )
                    )
                } else {
                    // Login gagal
                    showToast(context, "Autentikasi Gagal: ${task.exception?.message}")
                }
            }
    }

    private fun save(session: UserModel) {
        lifecycleScope.launch {
            viewModel.saveSession(session)
            val intent = Intent(this@SignInActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            ViewModelFactory.clearInstance()
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBarSignin.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.signinButton.isEnabled = !isLoading
        binding.googleSignInButton.isEnabled = !isLoading
    }
}