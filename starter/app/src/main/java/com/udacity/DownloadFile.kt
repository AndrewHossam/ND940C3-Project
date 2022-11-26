package com.udacity

import androidx.annotation.StringRes

data class DownloadFile(
    @StringRes
    val name: Int,
    val url: String
)

val downloadList = listOf(
    DownloadFile(name = R.string.glide_text, url = "https://github.com/bumptech/glide"),
    DownloadFile(name = R.string.load_app_text,
        url = "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter"),
    DownloadFile(name = R.string.retrofit_text, url = "https://github.com/square/retrofit")
)