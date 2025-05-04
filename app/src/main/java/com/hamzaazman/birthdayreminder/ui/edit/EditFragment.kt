package com.hamzaazman.birthdayreminder.ui.edit

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.hamzaazman.birthdayreminder.databinding.FragmentEditBinding
import com.hamzaazman.birthdayreminder.domain.model.Person
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class EditFragment : Fragment() {
    private var _binding: FragmentEditBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EditViewModel by viewModels()
    private val args: EditFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val person = args.person
        var selectedDate: LocalDate? = null

        with(binding) {
            person.let {
                nameInput.setText(it.name)
                birthDateInput.setText(it.birthDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                selectedDate = it.birthDate
            }

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

// ✅ Klavye açılmasın ve direk DatePicker gelsin
            birthDateInput.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    showDatePicker()
                    // Klavyeyi kapat
                    birthDateInput.clearFocus()
                }
            }

            saveButton.setOnClickListener {
                val name = nameInput.text.toString()
                val birthDateStr = birthDateInput.text.toString()

                if (name.isNotEmpty() && birthDateStr.isNotEmpty()) {
                    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy") // 👈 dikkat!
                    val birthDate = LocalDate.parse(birthDateStr, formatter)

                    val updatedPerson = Person(
                        id = person.id,
                        name = name,
                        birthDate = birthDate
                    )

                    viewModel.savePerson(updatedPerson) {
                        findNavController().popBackStack()
                    }
                } else {
                    Toast.makeText(requireContext(), "Tüm alanları doldurun", Toast.LENGTH_SHORT)
                        .show()
                }
            }


        }


    }
}