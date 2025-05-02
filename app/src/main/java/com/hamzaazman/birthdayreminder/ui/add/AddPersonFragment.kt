package com.hamzaazman.birthdayreminder.ui.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.hamzaazman.birthdayreminder.databinding.FragmentAddPersonBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddPersonFragment : Fragment() {

    private var _binding: FragmentAddPersonBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddPersonViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddPersonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}