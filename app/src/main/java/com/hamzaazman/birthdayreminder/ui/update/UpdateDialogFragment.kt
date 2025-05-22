package com.hamzaazman.birthdayreminder.ui.update

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import com.hamzaazman.birthdayreminder.R
import com.hamzaazman.birthdayreminder.common.ApkDownloader
import com.hamzaazman.birthdayreminder.data.model.UpdateInfo
import java.io.File
import kotlin.system.exitProcess


class UpdateDialogFragment(
    private val updateInfo: UpdateInfo
) : DialogFragment() {

    private lateinit var progressBar: ProgressBar
    private lateinit var btnUpdate: Button
    private lateinit var tvStatus: TextView

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_update, null)

        progressBar = view.findViewById(R.id.progressBar)
        btnUpdate = view.findViewById(R.id.btnUpdate)
        tvStatus = view.findViewById(R.id.tvStatus)

        builder.setView(view)
        val dialog = builder.create()

        btnUpdate.setOnClickListener {
            btnUpdate.isEnabled = false
            tvStatus.text = "İndiriliyor..."
            ApkDownloader.downloadApk(
                requireContext(),
                updateInfo.downloadUrl,
                onProgress = { progressBar.progress = it },
                onDownloaded = { apkFile ->
                    tvStatus.text = "İndirildi"
                    installApk(apkFile)
                    dialog.dismiss()
                }
            )
        }

        return dialog
    }

    private fun installApk(file: File) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(
                FileProvider.getUriForFile(
                    requireContext(),
                    "${requireContext().packageName}.provider",
                    file
                ),
                "application/vnd.android.package-archive"
            )
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
        requireContext().startActivity(intent)
        exitProcess(0)
    }
}
