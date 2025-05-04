package com.hamzaazman.birthdayreminder.ui.home

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.hamzaazman.birthdayreminder.common.collect
import com.hamzaazman.birthdayreminder.databinding.FragmentHomeBinding
import com.hamzaazman.birthdayreminder.ui.home.adapter.PersonAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<HomeViewModel>()
    private val todayAdapter by lazy {
        PersonAdapter()
    }
    private val allAdapter by lazy {
        PersonAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            todayRecyclerView.adapter = todayAdapter
            allRecyclerView.adapter = allAdapter

            todayAdapter.onItemClick = { person ->
                val action =
                    HomeFragmentDirections.actionHomeFragmentToEditFragment(person)
                findNavController().navigate(action)
            }
            allAdapter.onItemClick = { person ->
                val action =
                    HomeFragmentDirections.actionHomeFragmentToEditFragment(person)
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

                todayBirthdayTitle.isVisible = state.hasTodayBirthdays
                todayRecyclerView.isVisible = state.todayBirthdays.isNotEmpty()

                allTitle.isVisible = state.hasAllBirthdays
                allRecyclerView.isVisible = state.hasAllBirthdays
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}