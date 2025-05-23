package com.hamzaazman.birthdayreminder.ui.update

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hamzaazman.birthdayreminder.di.ApkDownloader
import com.hamzaazman.birthdayreminder.data.model.UpdateInfo
import com.hamzaazman.birthdayreminder.databinding.DialogUpdateBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import javax.inject.Inject
import kotlin.system.exitProcess

@AndroidEntryPoint
class UpdateDialogFragment(
    private val updateInfo: UpdateInfo
) : DialogFragment() {

    @Inject
    lateinit var apkDownloader: ApkDownloader

    private var _binding: DialogUpdateBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogUpdateBinding.inflate(LayoutInflater.from(context))
        val view = binding.root

        val builder = MaterialAlertDialogBuilder(requireContext())
        builder.setView(view)
        val dialog = builder.create()

        binding.btnUpdate.setOnClickListener {
            binding.btnUpdate.isEnabled = false
            binding.tvStatus.text = "İndiriliyor..."

            apkDownloader.downloadApk(
                updateInfo.downloadUrl,
                onProgress = { progress ->
                    binding.progressBar.progress = progress
                },
                onDownloaded = { apkFile ->
                    binding.tvStatus.text = "İndirildi"
                    installApk(apkFile)
                    dialog.dismiss()
                },
                onError = { exception ->
                    binding.tvStatus.text = "İndirme başarısız: ${exception.localizedMessage}"
                    binding.btnUpdate.isEnabled = true
                    Toast.makeText(requireContext(), "APK indirilemedi.", Toast.LENGTH_LONG).show()
                }
            )
        }

        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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