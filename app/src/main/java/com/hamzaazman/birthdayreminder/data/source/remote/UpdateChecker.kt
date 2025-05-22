package com.hamzaazman.birthdayreminder.data.source.remote

import android.content.Context
import com.hamzaazman.birthdayreminder.data.model.UpdateInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

class UpdateChecker(
    private val context: Context,
    private val updateUrl: String
) {
    suspend fun checkForUpdate(): UpdateInfo? = withContext(Dispatchers.IO) {
        try {
            val result = URL(updateUrl).readText()
            val json = JSONObject(result)

            val latestVersionCode = json.getInt("versionCode")
            val currentVersionCode = context.packageManager
                .getPackageInfo(context.packageName, 0).versionCode

            if (latestVersionCode > currentVersionCode) {
                return@withContext UpdateInfo(
                    versionCode = latestVersionCode,
                    versionName = json.getString("versionName"),
                    downloadUrl = json.getString("apkUrl"),
                    changelog = json.optString("changelog", "")
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return@withContext null
    }
}