package com.hamzaazman.birthdayreminder.ui.add

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.hamzaazman.birthdayreminder.R
import com.hamzaazman.birthdayreminder.common.TurkishDateFormatter
import com.hamzaazman.birthdayreminder.common.createBirthDatePicker
import com.hamzaazman.birthdayreminder.common.formatTurkish
import com.hamzaazman.birthdayreminder.common.setupDatePicker
import com.hamzaazman.birthdayreminder.common.viewBinding
import com.hamzaazman.birthdayreminder.databinding.FragmentAddPersonBinding
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate

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
            val picker = createBirthDatePicker(selectedDate) { selected ->
                selectedDate = selected
                binding.birthDateInput.setText(
                    selected.format(TurkishDateFormatter)
                )
            }
            picker.show(parentFragmentManager, "DATE_PICKER")
        }

        birthDateInput.setupDatePicker {
            showDatePicker()
        }

        saveButton.setOnClickListener {
            val name = nameInput.text.toString()
            val date = selectedDate

            if (name.isBlank() || date == null) {
                Toast.makeText(requireContext(), "Tüm alanları doldurun", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.savePerson(name, date.formatTurkish) {
                findNavController().popBackStack()
            }
        }

    }

}