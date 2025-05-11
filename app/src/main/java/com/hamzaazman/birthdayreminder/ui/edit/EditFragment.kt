package com.hamzaazman.birthdayreminder.ui.edit

import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hamzaazman.birthdayreminder.R
import com.hamzaazman.birthdayreminder.common.TurkishDateFormatter
import com.hamzaazman.birthdayreminder.common.createBirthDatePicker
import com.hamzaazman.birthdayreminder.common.setupDatePicker
import com.hamzaazman.birthdayreminder.common.viewBinding
import com.hamzaazman.birthdayreminder.databinding.FragmentEditBinding
import com.hamzaazman.birthdayreminder.domain.model.Person
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class EditFragment : Fragment(R.layout.fragment_edit) {
    private val binding by viewBinding(FragmentEditBinding::bind)

    private val viewModel: EditViewModel by viewModels()
    private val args: EditFragmentArgs by navArgs()
    private var selectedDate: LocalDate? = null

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.person_item_menu, menu)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            bindPersonData()
            setupToolbar()
            setupListeners()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun bindPersonData() = with(binding) {
        val person = args.person
        nameInput.setText(person.name)
        birthDateInput.setText(person.birthDate.format(TurkishDateFormatter))
        selectedDate = person.birthDate
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

        saveButton.setOnClickListener { savePerson() }
    }

    private fun setupToolbar() = with(binding) {
        toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        toolbar.inflateMenu(R.menu.person_item_menu)

        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_delete -> {
                    showDeleteDialog()
                    true
                }

                else -> false
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun savePerson() = with(binding) {
        val name = nameInput.text.toString()
        val birthDateStr = birthDateInput.text.toString()

        if (name.isNotEmpty() && birthDateStr.isNotEmpty()) {
            val birthDate = LocalDate.parse(birthDateStr, TurkishDateFormatter)

            val updatedPerson = Person(
                id = args.person.id,
                name = name,
                birthDate = birthDate
            )

            viewModel.savePerson(updatedPerson) {
                findNavController().popBackStack()
            }
        } else {
            Toast.makeText(requireContext(), "Tüm alanları doldurun", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showDeleteDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Kişiyi Sil")
            .setMessage("Bu kişiyi silmek istediğine emin misin?")
            .setPositiveButton("Evet") { _, _ ->
                viewModel.deletePerson(args.person.id) {
                    findNavController().popBackStack()
                }
            }
            .setNegativeButton("İptal", null)
            .show()
    }
}