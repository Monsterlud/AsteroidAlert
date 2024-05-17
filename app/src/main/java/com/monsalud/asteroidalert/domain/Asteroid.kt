package com.monsalud.asteroidalert.domain

import android.os.Parcelable
import com.monsalud.asteroidalert.data.local.room.AsteroidEntity
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Asteroid(
    val id: Long,
    val codename: String,
    val closeApproachDate: String,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean
) : Parcelable

fun List<Asteroid>.contentDeepEquals(other: List<Asteroid>): Boolean {
    if (this.size != other.size) return false

    val thisSet = this.toSet()
    val otherSet = other.toSet()

    return thisSet == otherSet
}

fun List<Asteroid>.asDatabaseModel(): List<AsteroidEntity> {
    return map {
        AsteroidEntity(
            asteroidId = it.id,
            codeName = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyDangerous = it.isPotentiallyHazardous
        )
    }
}

