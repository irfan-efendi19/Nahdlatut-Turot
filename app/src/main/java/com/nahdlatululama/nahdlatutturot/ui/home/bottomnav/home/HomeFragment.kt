package com.nahdlatululama.nahdlatutturot.ui.home.bottomnav.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.nahdlatululama.nahdlatutturot.R
import com.nahdlatululama.nahdlatutturot.ViewModelFactory
import com.nahdlatululama.nahdlatutturot.adapter.CardAdapter
import com.nahdlatululama.nahdlatutturot.adapter.KitabHomeAdapter
import com.nahdlatululama.nahdlatutturot.data.networking.repository.ResultData
import com.nahdlatululama.nahdlatutturot.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: KitabHomeAdapter
    private lateinit var categoryBooksAdapter1: KitabHomeAdapter
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory.getInstance(requireContext())
        viewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]

        setupRecyclerViews()
        observeViewModel()

        val viewPager: ViewPager2 = view.findViewById(R.id.card_slide)

        val images = listOf(
            R.drawable.banner,
            R.drawable.banner,
        )

        viewPager.adapter = CardAdapter(images)
    }

    private fun setupRecyclerViews() {
        adapter = KitabHomeAdapter()
        binding.rvAllBook.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = adapter
        }

//         Setup RecyclerView for Category Books
//        categoryBooksAdapter1 = KitabHomeAdapter()
//        binding.rvCategoryBooks.apply {
//            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
//            adapter = categoryBooksAdapter
//        }
    }

    private fun observeViewModel() {
        viewModel.books.observe(viewLifecycleOwner) { result ->
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
                    Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
                    Log.e("Error :", result.error)
                }
            }
        }

        // Observe Category Books
//        viewModel.booksByCategory1.observe(viewLifecycleOwner) { result ->
//            when (result) {
//                is ResultData.Loading -> {
//                    binding.progressBar.visibility = View.VISIBLE
//                }
//                is ResultData.Success -> {
//                    binding.progressBar.visibility = View.GONE
//                    adapterCategory1.submitList(result.data)
//                }
//                is ResultData.Error -> {
//                    binding.progressBar.visibility = View.GONE
//                    Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
//                    Log.e("Error :", result.error)
//                }
//            }
//        }

        // Mengamati kategori kedua
        viewModel.booksByCategory2.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultData.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is ResultData.Success -> {
                    binding.progressBar.visibility = View.GONE
//                    adapterCategory2.submitList(result.data)
                }
                is ResultData.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
                    Log.e("Error :", result.error)
                }
            }
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
