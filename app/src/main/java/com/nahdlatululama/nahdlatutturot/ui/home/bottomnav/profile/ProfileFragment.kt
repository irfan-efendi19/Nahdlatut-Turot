package com.nahdlatululama.nahdlatutturot.ui.home.bottomnav.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.nahdlatululama.nahdlatutturot.ViewModelFactory
import com.nahdlatululama.nahdlatutturot.data.networking.userPreference.UserPreference
import com.nahdlatululama.nahdlatutturot.data.networking.userPreference.dataStore
import com.nahdlatululama.nahdlatutturot.databinding.FragmentProfileBinding
import com.nahdlatululama.nahdlatutturot.ui.signin.SignInActivity
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userPreference = UserPreference.getInstance(requireContext().dataStore)

        lifecycleScope.launch {
            userPreference.getSession().collect { user ->
                val userId = user.userId
                val token = user.token
                val isLogin = user.isLogin

                Log.d("DEBUG_SESSION", "Retrieved Session: userId=$userId, token=$token, isLogin=$isLogin")

                if (userId.isNotEmpty() && token.isNotEmpty()) {
                    viewModel.getUserDetail(userId, token)
                } else {
                    Log.e("DEBUG_SESSION", "Invalid session! Redirecting to login...")
                    startActivity(Intent(requireContext(), SignInActivity::class.java))
                    requireActivity().finish()
                }
            }
        }

        viewModel.userDetail.observe(viewLifecycleOwner) { user ->
//            binding.txtName.text = user?.name ?: "No Data"
//            binding.txtEmail.text = user?.email ?: "No Data"
        }

        binding.btnlogout.setOnClickListener {
            lifecycleScope.launch {
                viewModel.logout()
                startActivity(Intent(requireContext(), SignInActivity::class.java))
                requireActivity().finish()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
