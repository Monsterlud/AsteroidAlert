package com.monsalud.asteroidalert.presentation.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.monsalud.asteroidalert.data.local.room.AsteroidDatabaseDao
import com.monsalud.asteroidalert.data.local.room.AsteroidEntity
import com.monsalud.asteroidalert.domain.Asteroid

class MainViewModel(
    private val dataSource: AsteroidDatabaseDao,
) : ViewModel() {

    private val _asteroids = MutableLiveData<AsteroidEntity>()
    val asteroids: LiveData<AsteroidEntity>
        get() = _asteroids

    private val _navigateToAsteroidDetail = MutableLiveData<Long?>()
    val navigateToAsteroidDetail: LiveData<Long?>
        get() = _navigateToAsteroidDetail

    fun getAllAsteroids() = dataSource.getAllAsteroids()

    fun addAllAsteroids() {
        val fakeAsteroids = getFakeAsteroidList()
        dataSource.insertAllAsteroids(*fakeAsteroids.toTypedArray())
    }

    fun onAsteroidItemClicked(asteroidId: Long) {
        _navigateToAsteroidDetail.value = asteroidId
    }

    fun doneNavigating() {
        _navigateToAsteroidDetail.value = null
    }

    private fun getFakeAsteroidList(): List<AsteroidEntity> {
        val asteroids = listOf(
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
        val asteroidEntities = convertAsteroidsToEntities(asteroids)
        return asteroidEntities
    }

    private fun convertAsteroidsToEntities(asteroids: List<Asteroid>): List<AsteroidEntity> {
        var asteroidEntities = mutableListOf<AsteroidEntity>()
        for (asteroid in asteroids) {
            asteroidEntities.add(
                AsteroidEntity(
                    asteroidId = asteroid.id,
                    codeName = asteroid.codename,
                    closeApproachDate = asteroid.closeApproachDate,
                    absoluteMagnitude = asteroid.absoluteMagnitude,
                    estimatedDiameter = asteroid.estimatedDiameter,
                    relativeVelocity = asteroid.relativeVelocity,
                    distanceFromEarth = asteroid.distanceFromEarth,
                    isPotentiallyDangerous = asteroid.isPotentiallyHazardous
                )
            )
        }
        return asteroidEntities
    }
}
