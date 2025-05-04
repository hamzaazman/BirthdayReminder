package com.hamzaazman.birthdayreminder.ui.home.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hamzaazman.birthdayreminder.common.daysUntilNextBirthday
import com.hamzaazman.birthdayreminder.databinding.AllPersonItemBinding
import com.hamzaazman.birthdayreminder.domain.model.Person
import java.time.format.DateTimeFormatter

class AllPersonAdapter : ListAdapter<Person, AllPersonAdapter.PersonViewHolder>(DiffCallback()) {

    var onItemClick: ((Person) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val binding = AllPersonItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PersonViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PersonViewHolder(private val binding: AllPersonItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(person: Person) {
            val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

            val birthDate = person.birthDate
            binding.textName.text = person.name
            binding.textBirthDate.text = birthDate.format(formatter)

            val daysLeft = daysUntilNextBirthday(birthDate)

            binding.textDayLeft.text = when (daysLeft) {
                0L -> "Bugün 🎉"
                1L -> "Yarın 🎈"
                else -> "$daysLeft gün"
            }

            binding.root.setOnClickListener {
                onItemClick?.invoke(person)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Person>() {
        override fun areItemsTheSame(oldItem: Person, newItem: Person): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Person, newItem: Person): Boolean =
            oldItem == newItem
    }
}
