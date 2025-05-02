package com.hamzaazman.birthdayreminder.ui.add

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.hamzaazman.birthdayreminder.databinding.FragmentAddPersonBinding
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class AddPersonFragment : Fragment() {

    private var _binding: FragmentAddPersonBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddPersonViewModel by viewModels()
    private var selectedDate: LocalDate? = null // DIŞARIDA tanımlanmalı

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddPersonBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        with(binding) {

            birthDateInput.setOnClickListener {
                val now = LocalDate.now()
                val datePicker = DatePickerDialog(
                    requireContext(),
                    { _, year, month, dayOfMonth ->
                        selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                        birthDateInput.setText(selectedDate?.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                    },
                    now.year,
                    now.monthValue - 1,
                    now.dayOfMonth
                )
                datePicker.show()
            }

            saveButton.setOnClickListener {
                val name = nameInput.text.toString()

                if (name.isNotBlank() && selectedDate != null) {
                    val birthDateStr =
                        selectedDate!!.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                    viewModel.savePerson(name, birthDateStr) {
                        findNavController().popBackStack()
                    }
                } else {
                    Toast.makeText(requireContext(), "Tüm alanları doldurun", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}