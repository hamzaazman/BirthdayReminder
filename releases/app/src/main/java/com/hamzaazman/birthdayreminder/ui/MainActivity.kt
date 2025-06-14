package com.hamzaazman.birthdayreminder.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.hamzaazman.birthdayreminder.R
import com.hamzaazman.birthdayreminder.data.source.remote.UpdateManager
import com.hamzaazman.birthdayreminder.databinding.ActivityMainBinding
import com.hamzaazman.birthdayreminder.ui.update.UpdateDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var updateManager: UpdateManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //checkForUpdates()
    }

    private fun checkForUpdates() {
        lifecycleScope.launch {
            updateManager.checkForUpdate(
                updateUrl = "https://hamzaazman.github.io/BirthdayReminder/releases/update.json",
                onUpdateAvailable = { updateInfo ->
                    UpdateDialogFragment(updateInfo).show(
                        supportFragmentManager,
                        "update_dialog"
                    )
                },
                onError = { exception ->
                    Toast.makeText(
                        this@MainActivity,
                        "Güncelleme kontrolü başarısız: ${exception.localizedMessage}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            )
        }
    }
}