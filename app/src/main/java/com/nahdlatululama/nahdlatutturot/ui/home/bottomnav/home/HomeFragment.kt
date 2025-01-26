package com.nahdlatululama.nahdlatutturot.ui.home.bottomnav.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.nahdlatululama.nahdlatutturot.R
import com.nahdlatululama.nahdlatutturot.ViewModelFactory
import com.nahdlatululama.nahdlatutturot.adapter.CardAdapter
import com.nahdlatululama.nahdlatutturot.adapter.KitabHomeAdapter
import com.nahdlatululama.nahdlatutturot.data.entity.BannerEntity
import com.nahdlatululama.nahdlatutturot.data.networking.repository.ResultData
import com.nahdlatululama.nahdlatutturot.databinding.FragmentHomeBinding
import me.relex.circleindicator.CircleIndicator
import me.relex.circleindicator.CircleIndicator3

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var kitabAdapter: KitabHomeAdapter
    private lateinit var viewModel: HomeViewModel

    private lateinit var viewPagerAuto: ViewPager2
    private lateinit var tabLayout: CircleIndicator3
    private val slideHandler = Handler(Looper.getMainLooper())

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


        viewPagerAuto = view.findViewById(R.id.card_slide)
        tabLayout = view.findViewById(R.id.tablayout)


        val banners = listOf(
            BannerEntity(R.drawable.banner, "https://www.example.com/1"),
            BannerEntity(R.drawable.banner, "https://www.example.com/2")
        )

        viewPagerAuto.adapter = CardAdapter(banners)


//        val images = listOf(
//            R.drawable.banner,
//            R.drawable.banner,
//        )
//
//        viewPagerAuto.adapter = CardAdapter(images)

        tabLayout.setViewPager(viewPagerAuto)

        startAutoSlide()
    }

    private fun startAutoSlide() {
        slideHandler.postDelayed(object : Runnable {
            override fun run() {
                val nextItem = (viewPagerAuto.currentItem + 1) % viewPagerAuto.adapter!!.itemCount
                viewPagerAuto.setCurrentItem(nextItem, true)
                slideHandler.postDelayed(this, 3000) // Slide setiap 3 detik
            }
        }, 3000)
    }

    private fun setupRecyclerViews() {
        kitabAdapter = KitabHomeAdapter()
        binding.rvAllBook.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false).apply {
                stackFromEnd = true
            }
            adapter = kitabAdapter
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
                    kitabAdapter.submitList(result.data)
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
//        viewModel.booksByCategory2.observe(viewLifecycleOwner) { result ->
//            when (result) {
//                is ResultData.Loading -> {
//                    binding.progressBar.visibility = View.VISIBLE
//                }
//                is ResultData.Success -> {
//                    binding.progressBar.visibility = View.GONE
////                    adapterCategory2.submitList(result.data)
//                }
//                is ResultData.Error -> {
//                    binding.progressBar.visibility = View.GONE
//                    Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
//                    Log.e("Error :", result.error)
//                }
//            }
//        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
