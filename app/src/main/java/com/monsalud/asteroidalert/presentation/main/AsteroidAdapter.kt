package com.monsalud.asteroidalert.presentation.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.monsalud.asteroidalert.R
import com.monsalud.asteroidalert.domain.Asteroid

class AsteroidAdapter : RecyclerView.Adapter<AsteroidAdapter.AsteroidViewHolder>() {
    private var asteroidList = getFakeAsteroidList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.list_item_asteroid, parent, false)
        return AsteroidViewHolder(view)
    }

    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
        val item = asteroidList[position]
        holder.asteroidTitle.text = item.codename
        holder.asteroidDate.text = item.closeApproachDate
        if (!item.isPotentiallyHazardous) {
            holder.asteroidStatus.setImageResource(R.drawable.ic_status_normal)
        } else {
            holder.asteroidStatus.setImageResource(R.drawable.ic_status_potentially_hazardous)
        }
    }

    override fun getItemCount(): Int = asteroidList.size

    class AsteroidViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val asteroidTitle: TextView = itemView.findViewById<TextView>(R.id.tv_asteroid_title)
        val asteroidDate: TextView = itemView.findViewById<TextView>(R.id.tv_asteroid_date)
        val asteroidStatus: ImageView = itemView.findViewById<ImageView>(R.id.iv_asteroid_status)
    }
}

fun getFakeAsteroidList(): List<Asteroid> {
    return listOf(
        Asteroid(1, "Asteroid1", "2024-06-01", 1.0, 100.0, 500.0, 10_000.0, false),
        Asteroid(2, "Asteroid2", "2024-06-02", 1.0, 100.0, 500.0, 10_000.0, true),
        Asteroid(3, "Asteroid3", "2024-06-03", 1.0, 100.0, 500.0, 10_000.0, false),
        Asteroid(4, "Asteroid4", "2024-06-04", 1.0, 100.0, 500.0, 10_000.0, true),
        Asteroid(5, "Asteroid5", "2024-06-05", 1.0, 100.0, 500.0, 10_000.0, false),
        Asteroid(6, "Asteroid6", "2024-06-06", 1.0, 100.0, 500.0, 10_000.0, true),
        Asteroid(7, "Asteroid7", "2024-06-07", 1.0, 100.0, 500.0, 10_000.0, false),
        Asteroid(8, "Asteroid8", "2024-06-08", 1.0, 100.0, 500.0, 10_000.0, true),
        Asteroid(9, "Asteroid9", "2024-06-09", 1.0, 100.0, 500.0, 10_000.0, false),
        Asteroid(10, "Asteroid10", "2024-06-10", 1.0, 100.0, 500.0, 10_000.0, true),
        Asteroid(11, "Asteroid11", "2024-06-11", 1.0, 100.0, 500.0, 10_000.0, false),
        Asteroid(12, "Asteroid12", "2024-06-12", 1.0, 100.0, 500.0, 10_000.0, true),
        Asteroid(13, "Asteroid13", "2024-06-13", 1.0, 100.0, 500.0, 10_000.0, false),
        Asteroid(14, "Asteroid14", "2024-06-14", 1.0, 100.0, 500.0, 10_000.0, true),
        Asteroid(15, "Asteroid15", "2024-06-15", 1.0, 100.0, 500.0, 10_000.0, false),
        Asteroid(16, "Asteroid16", "2024-06-16", 1.0, 100.0, 500.0, 10_000.0, true),
        Asteroid(17, "Asteroid17", "2024-06-17", 1.0, 100.0, 500.0, 10_000.0, false),
        Asteroid(18, "Asteroid18", "2024-06-18", 1.0, 100.0, 500.0, 10_000.0, true),
        Asteroid(19, "Asteroid19", "2024-06-19", 1.0, 100.0, 500.0, 10_000.0, false),
        Asteroid(20, "Asteroid20", "2024-06-20", 1.0, 100.0, 500.0, 10_000.0, true)
    )
}
