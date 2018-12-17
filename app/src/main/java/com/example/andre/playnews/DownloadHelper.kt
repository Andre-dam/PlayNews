package com.example.andre.playnews

import android.os.AsyncTask
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets

class DownloadHelper : AsyncTask<String, String, String>() {
    override fun doInBackground(vararg params: String?): String {
        val url = URL(params[0])
        val urlConnection = url.openConnection() as HttpURLConnection
        val resp = BufferedInputStream(urlConnection.inputStream)
        return BufferedReader(InputStreamReader(resp, StandardCharsets.UTF_8)).readText()
    }
}