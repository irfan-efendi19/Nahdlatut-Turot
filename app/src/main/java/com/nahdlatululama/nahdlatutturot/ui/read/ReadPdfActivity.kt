package com.nahdlatululama.nahdlatutturot.ui.read

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.nahdlatululama.nahdlatutturot.databinding.ActivityReadPdfBinding
import com.rajat.pdfviewer.PdfViewerActivity
import com.rajat.pdfviewer.util.saveTo

class ReadPdfActivity : AppCompatActivity() {

    private var _binding: ActivityReadPdfBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityReadPdfBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pdfUrl = intent.getStringExtra(PDF_URL)

        if (pdfUrl.isNullOrEmpty()) {
            Toast.makeText(this, "URL PDF tidak tersedia", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            PdfViewerActivity.launchPdfFromUrl(
                context = this,
                pdfUrl = pdfUrl.toString(),
                pdfTitle = "Document Viewer",
                saveTo = saveTo.ASK_EVERYTIME,
                enableDownload = true
            )
        }

        binding.pdfView.initWithUrl(
            url = pdfUrl.toString(),
            lifecycleCoroutineScope = lifecycleScope,
            lifecycle = lifecycle
        )

        Log.e("PDF : ",pdfUrl.toString())
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

