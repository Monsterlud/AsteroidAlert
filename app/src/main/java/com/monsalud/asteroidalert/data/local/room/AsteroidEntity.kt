package com.monsalud.asteroidalert.data.local.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.monsalud.asteroidalert.domain.Asteroid

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

fun List<AsteroidEntity>.asDomainModel(): List<Asteroid> {
    return map {
        Asteroid(
            id = it.asteroidId,
            codename = it.codeName,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyDangerous
        )
    }
}

fun AsteroidEntity.asDomainModel(): Asteroid {
    return Asteroid(
        id = asteroidId,
        codename = codeName,
        closeApproachDate = closeApproachDate,
        absoluteMagnitude = absoluteMagnitude,
        estimatedDiameter = estimatedDiameter,
        relativeVelocity = relativeVelocity,
        distanceFromEarth = distanceFromEarth,
        isPotentiallyHazardous = isPotentiallyDangerous
    )
}
