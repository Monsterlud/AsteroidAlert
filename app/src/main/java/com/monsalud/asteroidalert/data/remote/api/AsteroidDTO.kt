package com.monsalud.asteroidalert.data.remote.api

import com.monsalud.asteroidalert.data.local.room.AsteroidEntity
import com.squareup.moshi.JsonClass

data class AsteroidContainer(val asteroids: List<AsteroidDTO>)
@JsonClass(generateAdapter = true)
class AsteroidDTO(
    val id: Long,
    val codename: String,
    val closeApproachDate: String,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean
)

fun AsteroidContainer.asDatabaseModel() : Array<AsteroidEntity> {
    return asteroids.map {
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
    }.toTypedArray()
}