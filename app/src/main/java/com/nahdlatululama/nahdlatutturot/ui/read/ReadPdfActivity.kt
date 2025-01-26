package com.nahdlatululama.nahdlatutturot.ui.read

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.nahdlatululama.nahdlatutturot.databinding.ActivityReadPdfBinding
import com.rajat.pdfviewer.HeaderData
import com.rajat.pdfviewer.PdfDownloader
import com.rajat.pdfviewer.PdfViewerActivity
import com.rajat.pdfviewer.util.saveTo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL

class ReadPdfActivity : AppCompatActivity() {

    private var _binding: ActivityReadPdfBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout
        _binding = ActivityReadPdfBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get PDF URL from Intent
        val pdfUrl = intent.getStringExtra(PDF_URL)

        if (pdfUrl.isNullOrEmpty()) {
            Toast.makeText(this, "URL PDF tidak tersedia", Toast.LENGTH_SHORT).show()
            finish()
            return // Early exit if URL is invalid
        }

        // Log the PDF URL for debugging
        Log.e("ReadPdfActivity", "Loading PDF: $pdfUrl")

        // Download PDF manually with timeout
        lifecycleScope.launch {
            try {
                val pdfFile = downloadPdfWithTimeout(pdfUrl, 60_000) // 60 detik timeout
                binding.pdfView.initWithFile(pdfFile)
            } catch (e: Exception) {
                Log.e("ReadPdfActivity", "Error downloading or displaying PDF", e)
                Toast.makeText(this@ReadPdfActivity, "Gagal memuat PDF: ${e.message}", Toast.LENGTH_SHORT).show()
                finish()
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null // Avoid memory leaks
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









