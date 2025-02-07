package com.nahdlatululama.nahdlatutturot.ui.home.bottomnav.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.nahdlatululama.nahdlatutturot.ViewModelFactory
import com.nahdlatululama.nahdlatutturot.adapter.SearchAdapter
import com.nahdlatululama.nahdlatutturot.data.networking.repository.ResultData
import com.nahdlatululama.nahdlatutturot.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: SearchAdapter
    private lateinit var viewModel: SearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory.getInstance(requireContext())
        viewModel = ViewModelProvider(this, factory)[SearchViewModel::class.java]

        setupRecyclerView()
        observeViewModel()
        setupSearchBar()
    }

    private fun setupRecyclerView() {
        adapter = SearchAdapter()
        binding.rvSearchResult.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@SearchFragment.adapter
        }
    }

    private fun observeViewModel() {
        viewModel.searchResults.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultData.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is ResultData.Success -> {
                    binding.progressBar.visibility = View.GONE
                    adapter.submitList(result.data)
                }
                is ResultData.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), "Tidak Menemukan Kitab", Toast.LENGTH_SHORT).show()
                    Log.e("SearchFragment", "Error: ${result.error}")
                }
            }
        }
    }

    private fun setupSearchBar() {
        binding.searchBar.setOnClickListener {
            binding.searchView.show()
        }

        binding.searchView.editText.setOnEditorActionListener { textView, _, _ ->
            val keyword = textView.text.toString().trim()
            if (keyword.isNotEmpty()) {
                viewModel.searchBooks(keyword)
                binding.searchView.hide()
            } else {
                Toast.makeText(requireContext(), "Masukkan kata kunci", Toast.LENGTH_SHORT).show()
            }
            true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


