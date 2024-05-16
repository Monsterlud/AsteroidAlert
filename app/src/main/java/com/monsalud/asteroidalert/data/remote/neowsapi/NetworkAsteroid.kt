package com.monsalud.asteroidalert.data.remote.neowsapi

import com.monsalud.asteroidalert.data.local.room.AsteroidEntity

data class NetworkAsteroidContainer(val near_earth_objects: Map<String, List<NetworkAsteroid>>)
data class NetworkAsteroid(
    val absolute_magnitude_h: Double,
    val close_approach_data: List<CloseApproachData>,
    val estimated_diameter: EstimatedDiameter,
    val id: String,
    val is_potentially_hazardous_asteroid: Boolean,
    val is_sentry_object: Boolean,
    val links: Links,
    val name: String,
    val nasa_jpl_url: String,
    val neo_reference_id: String
)

data class CloseApproachData(
    val close_approach_date: String,
    val close_approach_date_full: String,
    val epoch_date_close_approach: Long,
    val miss_distance: MissDistance,
    val orbiting_body: String,
    val relative_velocity: RelativeVelocity
)

data class EstimatedDiameter(
    val feet: Feet,
    val kilometers: Kilometers,
    val meters: Meters,
    val miles: Miles
)

data class Feet(
    val estimated_diameter_max: Double,
    val estimated_diameter_min: Double
)

data class Kilometers(
    val estimated_diameter_max: Double,
    val estimated_diameter_min: Double
)

data class Links(
    val self: String
)

data class Meters(
    val estimated_diameter_max: Double,
    val estimated_diameter_min: Double
)

data class Miles(
    val estimated_diameter_max: Double,
    val estimated_diameter_min: Double
)

data class MissDistance(
    val astronomical: String,
    val kilometers: String,
    val lunar: String,
    val miles: String
)

data class RelativeVelocity(
    val kilometers_per_hour: String,
    val kilometers_per_second: String,
    val miles_per_hour: String
)

fun List<NetworkAsteroid>.asDatabaseModel() : List<AsteroidEntity> {
    return map {
        AsteroidEntity(
            asteroidId = it.id.toLong(),
            codeName = it.name,
            closeApproachDate = it.close_approach_data.first().close_approach_date,
            absoluteMagnitude = it.absolute_magnitude_h,
            estimatedDiameter = it.estimated_diameter.kilometers.estimated_diameter_max,
            relativeVelocity = it.close_approach_data.first().relative_velocity.kilometers_per_second.toDouble(),
            distanceFromEarth = it.close_approach_data.first().miss_distance.astronomical.toDouble(),
            isPotentiallyDangerous = it.is_potentially_hazardous_asteroid
        )
    }
}