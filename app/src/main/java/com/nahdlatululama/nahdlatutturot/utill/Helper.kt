package com.nahdlatululama.nahdlatutturot.utill

import android.content.Context
import android.net.ConnectivityManager
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide

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


