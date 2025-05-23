package com.hamzaazman.birthdayreminder.ui.home

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.hamzaazman.birthdayreminder.R
import com.hamzaazman.birthdayreminder.common.collect
import com.hamzaazman.birthdayreminder.common.viewBinding
import com.hamzaazman.birthdayreminder.databinding.FragmentHomeBinding
import com.hamzaazman.birthdayreminder.ui.home.adapter.AllPersonAdapter
import com.hamzaazman.birthdayreminder.ui.home.adapter.TodayAdapter
import dagger.hilt.android.AndroidEntryPoint


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
    private val updateUrl = "https://hamzaazman.github.io/BirthdayReminder/releases/update.json"



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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