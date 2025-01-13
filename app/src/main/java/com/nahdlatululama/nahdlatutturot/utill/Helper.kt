package com.bangkit.caraka.utill

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.ConnectivityManager
import android.net.Uri
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.exifinterface.media.ExifInterface
import com.bumptech.glide.Glide
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun ImageView.loadImage(url: String) {
    Glide.with(this.context)
        .load(url)
        .into(this)
}

fun showToast(context: Context, message: String?) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}


 fun isNetworkConnected(context: Context): Boolean {
     val connectivityManager =
         context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
     val networkInfo = connectivityManager.activeNetworkInfo
     return networkInfo != null && networkInfo.isConnected
}


