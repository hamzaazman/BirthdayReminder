package com.hamzaazman.birthdayreminder.ui.add

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.hamzaazman.birthdayreminder.R
import com.hamzaazman.birthdayreminder.common.viewBinding
import com.hamzaazman.birthdayreminder.databinding.FragmentAddPersonBinding
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class AddFragment : Fragment(R.layout.fragment_add_person) {

    private val binding by viewBinding(FragmentAddPersonBinding::bind)

    private val viewModel: AddViewModel by viewModels()
    private var selectedDate: LocalDate? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            setupListeners()

        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupListeners() = with(binding) {
        val showDatePicker = {
            val now = LocalDate.now()
            val datePicker = DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                    birthDateInput.setText(
                        selectedDate?.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                    )
                },
                now.year,
                now.monthValue - 1,
                now.dayOfMonth
            )
            datePicker.show()
        }

        birthDateInput.setOnClickListener {
            showDatePicker()
        }

        birthDateInput.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                showDatePicker()
                birthDateInput.clearFocus()
            }
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