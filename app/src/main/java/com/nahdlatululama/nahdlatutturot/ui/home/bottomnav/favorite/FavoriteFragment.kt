package com.nahdlatululama.nahdlatutturot.ui.home.bottomnav.favorite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.nahdlatululama.nahdlatutturot.ViewModelFactory
import com.nahdlatululama.nahdlatutturot.adapter.FavoriteAdapter
import com.nahdlatululama.nahdlatutturot.data.entity.KitabEntityFavorite
import com.nahdlatululama.nahdlatutturot.data.networking.repository.ResultData
import com.nahdlatululama.nahdlatutturot.databinding.FragmentSavedBinding
import com.nahdlatululama.nahdlatutturot.ui.detail.DetailActivity

class FavoriteFragment : Fragment() {

    private var _binding: FragmentSavedBinding? = null
    private val binding get() = _binding!!
    private lateinit var username: String

    private val viewModel by activityViewModels<FavoriteViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSavedBinding.inflate(inflater, container, false)
        val view = binding.root

        username = arguments?.getString(DetailActivity.DETAIL_STORY).orEmpty()

        observer()
        setupFavoriteRV()

        return view
    }

    private fun observer() {
        viewModel.getUserFavorite().observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultData.Loading -> {
                    // Show loading indicator if needed
                }

                is ResultData.Success -> {
                    setupRvData(result.data)
                }

                is ResultData.Error -> {
                    Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
                }

                else -> {
                }
            }
        }
    }

    private fun setupRvData(responseItems: List<KitabEntityFavorite>) {
        val adapter = FavoriteAdapter().apply {
            differ.submitList(responseItems)
            onClick = { book ->
                val intent = Intent(requireContext(), DetailActivity::class.java).apply {
                    putExtra(DetailActivity.DETAIL_STORY, book) // Sending Parcelable data
                }
                startActivity(intent)
            }
        }
        with(binding) {
            rvFavoriteBook.adapter = adapter
            if (responseItems.isEmpty()) {
                rvFavoriteBook.visibility = View.GONE
                tvEmptyfav.visibility = View.VISIBLE
            } else {
                rvFavoriteBook.visibility = View.VISIBLE
                tvEmptyfav.visibility = View.GONE
            }
        }
    }


    private fun setupFavoriteRV() {
        binding.rvFavoriteBook.apply {
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


