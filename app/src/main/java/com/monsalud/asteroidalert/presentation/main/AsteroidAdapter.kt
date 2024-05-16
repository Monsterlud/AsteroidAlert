package com.monsalud.asteroidalert.presentation.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.monsalud.asteroidalert.R
import com.monsalud.asteroidalert.databinding.ListItemAsteroidBinding
import com.monsalud.asteroidalert.domain.Asteroid

class AsteroidAdapter(
    private val clickListener: AsteroidClickListener
) : ListAdapter<Asteroid, AsteroidAdapter.AsteroidViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {
        return AsteroidViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemView.setOnClickListener {
            clickListener.onClick(item)
        }
        holder.bind(item, clickListener)
    }

    class AsteroidViewHolder private constructor(private val binding: ListItemAsteroidBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: Asteroid,
            clickListener: AsteroidClickListener
        ) {
            binding.tvAsteroidTitle.text = item.codename
            binding.tvAsteroidDate.text = item.closeApproachDate
            binding.clickListener = clickListener
            binding.ivAsteroidStatus.setImageResource(
                when {
                    item.isPotentiallyHazardous -> R.drawable.ic_status_potentially_hazardous
                    else -> R.drawable.ic_status_normal
                }
            )
        }

        companion object {
            fun from(parent: ViewGroup): AsteroidViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemAsteroidBinding.inflate(layoutInflater,  parent, false)
                return AsteroidViewHolder(binding)
            }
        }
    }
}

class AsteroidClickListener(val clickListener: (asteroid: Asteroid) -> Unit) {
    fun onClick(asteroid: Asteroid) = clickListener(asteroid)
}
