package com.hamzaazman.birthdayreminder.ui.home.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hamzaazman.birthdayreminder.databinding.PersonRowItemBinding
import com.hamzaazman.birthdayreminder.domain.model.Person
import java.time.format.DateTimeFormatter


class PersonAdapter : ListAdapter<Person, PersonAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            PersonRowItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class ViewHolder(private val binding: PersonRowItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(person: Person) {
            binding.apply {
                personNameText.text = person.name
                personBirthdateText.text =
                    person.birthDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Person>() {
        override fun areItemsTheSame(oldItem: Person, newItem: Person) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Person, newItem: Person) =
            oldItem == newItem
    }
}