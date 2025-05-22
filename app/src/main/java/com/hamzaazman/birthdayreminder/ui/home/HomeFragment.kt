package com.hamzaazman.birthdayreminder.ui.home

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hamzaazman.birthdayreminder.R
import com.hamzaazman.birthdayreminder.common.ApkDownloader
import com.hamzaazman.birthdayreminder.common.collect
import com.hamzaazman.birthdayreminder.common.viewBinding
import com.hamzaazman.birthdayreminder.data.model.UpdateInfo
import com.hamzaazman.birthdayreminder.data.source.remote.UpdateChecker
import com.hamzaazman.birthdayreminder.databinding.FragmentHomeBinding
import com.hamzaazman.birthdayreminder.ui.home.adapter.AllPersonAdapter
import com.hamzaazman.birthdayreminder.ui.home.adapter.TodayAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding by viewBinding(FragmentHomeBinding::bind)

    private val viewModel by viewModels<HomeViewModel>()
    private val todayAdapter by lazy {
        TodayAdapter()
    }
    private val allAdapter by lazy {
        AllPersonAdapter()
    }
    private val updateUrl = "https://yourdomain.com/update.json"


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            val updateInfo = UpdateChecker(requireContext(), updateUrl).checkForUpdate()
            updateInfo?.let { showUpdateDialog(it) }
        }

        with(binding) {
            todayRecyclerView.adapter = todayAdapter
            allRecyclerView.adapter = allAdapter

            todayAdapter.onItemClick = { personId ->
                val action =
                    HomeFragmentDirections.actionHomeFragmentToEditFragment(personId)
                findNavController().navigate(action)
            }
            allAdapter.onItemClick = { personId ->
                val action =
                    HomeFragmentDirections.actionHomeFragmentToEditFragment(personId)
                findNavController().navigate(action)
            }

            fab.setOnClickListener {
                val action = HomeFragmentDirections.actionHomeFragmentToAddPersonFragment()
                findNavController().navigate(action)
            }
        }




        collectState()
    }

    private fun showUpdateDialog(updateInfo: UpdateInfo) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Yeni Güncelleme (${updateInfo.versionName})")
            .setMessage(updateInfo.changelog.ifEmpty { "Yeni bir güncelleme mevcut." })
            .setPositiveButton("Güncelle") { _, _ ->
                ApkDownloader(requireContext()).downloadAndInstall(updateInfo.downloadUrl)
            }
            .setNegativeButton("Sonra", null)
            .show()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun collectState() {
        with(binding) {
            viewModel.uiState.collect(viewLifecycleOwner) { state ->

                allAdapter.submitList(state.persons)
                todayAdapter.submitList(state.todayBirthdays)

                emptySection.isVisible = state.persons.isEmpty()

                todaySection.isVisible = state.hasTodayBirthdays

                //todayBirthdayTitle.isVisible = state.hasTodayBirthdays
                //todayRecyclerView.isVisible = state.todayBirthdays.isNotEmpty()


                allSection.isVisible = state.hasAllBirthdays

                //allTitle.isVisible = state.hasAllBirthdays
                //allRecyclerView.isVisible = state.hasAllBirthdays
            }
        }
    }


}