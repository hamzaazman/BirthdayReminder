package com.hamzaazman.birthdayreminder.common

import android.content.Context
import com.hamzaazman.birthdayreminder.data.model.UpdateInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

object UpdateManager {

    fun checkForUpdate(
        context: Context,
        updateUrl: String,
        onUpdateAvailable: (UpdateInfo) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = URL(updateUrl).readText()
                val json = JSONObject(result)

                val latestVersionCode = json.getInt("versionCode")
                val currentVersionCode = context.packageManager
                    .getPackageInfo(context.packageName, 0).versionCode

                if (latestVersionCode > currentVersionCode) {
                    val updateInfo = UpdateInfo(
                        versionCode = latestVersionCode,
                        versionName = json.getString("versionName"),
                        downloadUrl = json.getString("apkUrl"),
                        changelog = json.optString("changelog", "")
                    )
                    withContext(Dispatchers.Main) {
                        onUpdateAvailable(updateInfo)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
