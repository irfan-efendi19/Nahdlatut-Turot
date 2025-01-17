package com.nahdlatululama.nahdlatutturot.ui.home.bottomnav.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.search.SearchBar
import com.google.android.material.search.SearchView
import com.nahdlatululama.nahdlatutturot.R
import com.nahdlatululama.nahdlatutturot.adapter.KitabHomeAdapter
import com.nahdlatululama.nahdlatutturot.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private lateinit var searchEditText: EditText
    private lateinit var searchButton: ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: KitabHomeAdapter
    private val bookViewModel: SearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        searchEditText = view.findViewById(R.id.et_search)
//        searchButton = view.findViewById(R.id.btn_search)
//        recyclerView = view.findViewById(R.id.rv_search_results)
//        progressBar = view.findViewById(R.id.progress_bar)

        adapter = KitabHomeAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        bookViewModel.searchResults.observe(viewLifecycleOwner) { books ->
            adapter.submitList(books)
        }

        bookViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        searchButton.setOnClickListener {
            val keyword = searchEditText.text.toString().trim()
            if (keyword.isNotEmpty()) {
                bookViewModel.searchBooks(keyword)
            } else {
                Toast.makeText(requireContext(), "Enter a keyword", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
