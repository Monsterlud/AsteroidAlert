package com.monsalud.asteroidalert.data.local.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "incoming_asteroid_table")
data class AsteroidEntity constructor(
    @PrimaryKey(autoGenerate = true)
    var asteroidId: Long,

    @ColumnInfo(name = "code_name")
    val codeName: String,

    @ColumnInfo(name = "close_approach_date")
    val closeApproachDate: String,

    @ColumnInfo(name = "absolute_magnitude")
    val absoluteMagnitude: Double,

    @ColumnInfo(name = "estimated_diameter")
    val estimatedDiameter: Double,

    @ColumnInfo(name = "relative_ velocity")
    val relativeVelocity: Double,

    @ColumnInfo(name = "distance_from_earth")
    val distanceFromEarth: Double,

    @ColumnInfo(name = "is_potentially_dangerous")
    val isPotentiallyDangerous: Boolean
)