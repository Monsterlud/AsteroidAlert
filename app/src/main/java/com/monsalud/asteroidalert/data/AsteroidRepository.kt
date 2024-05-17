package com.monsalud.asteroidalert.data


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.monsalud.asteroidalert.data.local.room.AsteroidDatabase
import com.monsalud.asteroidalert.data.local.room.asDomainModel
import com.monsalud.asteroidalert.data.remote.neowsapi.NeoWsApi
import com.monsalud.asteroidalert.data.remote.Constants
import com.monsalud.asteroidalert.data.remote.apodapi.APODApi
import com.monsalud.asteroidalert.data.remote.apodapi.NetworkAPOD
import com.monsalud.asteroidalert.data.remote.neowsapi.asDatabaseModel
import com.monsalud.asteroidalert.data.remote.getEndSearchDate
import com.monsalud.asteroidalert.data.remote.getTodayDate
import com.monsalud.asteroidalert.domain.Asteroid
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AsteroidRepository(
    private val database: AsteroidDatabase
) {
    val asteroids: LiveData<List<Asteroid>> = database.asteroidDatabaseDao.getAllAsteroids().map {
        return@map it.asDomainModel()
    }

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            try {
                val existingAsteroidList = database.asteroidDatabaseDao.getAllAsteroids().value
                val newAsteroidList = NeoWsApi.retrofitService.getAsteroids(
                    getTodayDate(),
                    getEndSearchDate(),
                    Constants.API_KEY
                )
                    .await()
                    .near_earth_objects.values.flatten()
                    .asDatabaseModel()
                if (existingAsteroidList != null) {
                    if (existingAsteroidList.contentsDeepEquals(newAsteroidList)) {
                        database.asteroidDatabaseDao.clearDatabase()
                        database.asteroidDatabaseDao.insertAllAsteroids(*newAsteroidList.toTypedArray())
                    } else return@withContext
                }
            } catch (e: Exception) {
                Log.e("AsteroidRepository", "refreshAsteroids() error: $e")
            }
        }
    }

    private fun <T> List<T>.contentsDeepEquals(other: List<T>): Boolean {
        if (this.size != other.size) return false
        return this.mapIndexed { index, element -> element == other[index] }.all { it }
    }

    suspend fun clearAsteroids() {
        withContext(Dispatchers.IO) {
            try {
                database.asteroidDatabaseDao.clearDatabase()
            } catch (e: Exception) {
                Log.e("AsteroidRepository", "clearAsteroids() error")
            }
        }
    }

    suspend fun getApod(): NetworkAPOD? {
        return withContext(Dispatchers.IO) {
            try {
                val apod = APODApi.retrofitService.getApod(Constants.API_KEY, true).await()
                Log.i("AsteroidRepository", "image url: ${apod.url}")
                return@withContext apod
            } catch (e: Exception) {
                Log.e("AsteroidRepository", "getApod() error: ${e.message}")
                return@withContext null
            }
        }
    }
}
