package com.hamzaazman.birthdayreminder.common

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL


object ApkDownloader {

    fun downloadApk(
        context: Context,
        url: String,
        onProgress: (Int) -> Unit,
        onDownloaded: (File) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val connection = URL(url).openConnection() as HttpURLConnection
                connection.connect()

                val totalSize = connection.contentLength
                val inputStream = BufferedInputStream(connection.inputStream)
                val apkFile = File(context.getExternalFilesDir(null), "update.apk")
                val outputStream = FileOutputStream(apkFile)

                val buffer = ByteArray(1024)
                var count: Int
                var downloaded = 0

                while (inputStream.read(buffer).also { count = it } != -1) {
                    outputStream.write(buffer, 0, count)
                    downloaded += count
                    val progress = (downloaded * 100) / totalSize
                    withContext(Dispatchers.Main) {
                        onProgress(progress)
                    }
                }

                outputStream.flush()
                outputStream.close()
                inputStream.close()

                withContext(Dispatchers.Main) {
                    onDownloaded(apkFile)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
