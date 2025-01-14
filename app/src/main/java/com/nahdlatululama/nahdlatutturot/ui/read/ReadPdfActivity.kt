package com.nahdlatululama.nahdlatutturot.ui.read

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nahdlatululama.nahdlatutturot.databinding.ActivityReadPdfBinding

class ReadPdfActivity : AppCompatActivity() {

    private var _binding: ActivityReadPdfBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityReadPdfBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}