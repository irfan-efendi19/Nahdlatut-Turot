package com.nahdlatululama.nahdlatutturot.ui.read

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.nahdlatululama.nahdlatutturot.databinding.ActivityReadPdfBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.net.URL

class ReadPdfActivity : AppCompatActivity() {

    private var _binding: ActivityReadPdfBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityReadPdfBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()

        supportActionBar?.hide()
        val pdfUrl = intent.getStringExtra(PDF_URL)

        if (pdfUrl.isNullOrEmpty()) {
            Toast.makeText(this, "URL PDF tidak tersedia", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        Log.e("ReadPdfActivity", "Loading PDF: $pdfUrl")

        binding.progressBarKitab.visibility = View.VISIBLE
        binding.pdfView.visibility = View.GONE

        lifecycleScope.launch {
            try {
                val pdfFile = downloadPdfWithTimeout(pdfUrl, 60_000) // 60 detik timeout
                binding.pdfView.initWithFile(pdfFile)
            } catch (e: Exception) {
                Log.e("ReadPdfActivity", "Error downloading or displaying PDF", e)
                Toast.makeText(this@ReadPdfActivity, "Gagal memuat PDF: ${e.message}", Toast.LENGTH_SHORT).show()
                finish()
            } finally {
                binding.progressBarKitab.visibility = View.GONE
                binding.pdfView.visibility = View.VISIBLE
            }
        }
    }

    private suspend fun downloadPdfWithTimeout(url: String, timeout: Int): File {
        return withContext(Dispatchers.IO) {
            val connection = URL(url).openConnection().apply {
                connectTimeout = timeout
                readTimeout = timeout
            }
            val inputStream = BufferedInputStream(connection.getInputStream())
            val outputFile = File(cacheDir, "${url.hashCode()}.pdf")
            FileOutputStream(outputFile).use { outputStream ->
                val buffer = ByteArray(8192)
                var bytesRead: Int
                while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                    outputStream.write(buffer, 0, bytesRead)
                }
                outputStream.flush()
            }
            outputFile
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val PDF_URL = "pdf_url"

        fun start(context: Context, pdfUrl: String) {
            val intent = Intent(context, ReadPdfActivity::class.java)
            intent.putExtra(PDF_URL, pdfUrl)
            context.startActivity(intent)
        }
    }
}










