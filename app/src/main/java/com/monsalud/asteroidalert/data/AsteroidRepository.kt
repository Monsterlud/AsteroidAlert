package com.monsalud.asteroidalert.data


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.map
import com.monsalud.asteroidalert.data.local.room.AsteroidDatabase
import com.monsalud.asteroidalert.data.local.room.AsteroidEntity
import com.monsalud.asteroidalert.data.local.room.asDomainModel
import com.monsalud.asteroidalert.data.remote.neowsapi.NeoWsApi
import com.monsalud.asteroidalert.data.remote.Constants
import com.monsalud.asteroidalert.data.remote.apodapi.APODApi
import com.monsalud.asteroidalert.data.remote.apodapi.NetworkAPOD
import com.monsalud.asteroidalert.data.remote.neowsapi.asDatabaseModel
import com.monsalud.asteroidalert.data.remote.getEndSearchDate
import com.monsalud.asteroidalert.data.remote.getTodayDate
import com.monsalud.asteroidalert.data.remote.neowsapi.asDomainModel
import com.monsalud.asteroidalert.domain.Asteroid
import com.monsalud.asteroidalert.domain.asDatabaseModel
import com.monsalud.asteroidalert.domain.contentDeepEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

class AsteroidRepository(
    private val database: AsteroidDatabase
) {

    /**
     * LiveData holding the list of Asteroids that is mapped from AsteroidEntities
     */
    val asteroids: LiveData<List<Asteroid>> = database.asteroidDatabaseDao.getAllAsteroids().map {
        return@map it.asDomainModel()
    }

    /**
     * Main method that refreshes the list of Asteroids
     * This method compares the database list to the network list
     * If the lists are the same it just returns, assuring that the asteroids LiveData is not updated
     * If the lists are NOT the same the database is cleared and the network list is saved to the database,
     * updating the asteroid LiveData which updates the Views downstream
     *
     */
    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            try {
                val existingAsteroidList = asteroids.value
                val newAsteroidList = NeoWsApi.retrofitService.getAsteroids(
                    getTodayDate(),
                    getEndSearchDate(),
                    Constants.API_KEY
                )
                    .await()
                    .near_earth_objects.values.flatten()
                    .asDomainModel()
                if (existingAsteroidList != null) {
                    val equal = existingAsteroidList.contentDeepEquals(newAsteroidList)
                    if (!existingAsteroidList.contentDeepEquals(newAsteroidList)) {
                        database.asteroidDatabaseDao.clearDatabase()
                        val asteroidEntityList = newAsteroidList.asDatabaseModel()
                        database.asteroidDatabaseDao.insertAllAsteroids(*asteroidEntityList.toTypedArray())
                    } else
                        return@withContext
                }
            } catch (e: Exception) {
                Log.e("AsteroidRepository", "refreshAsteroids() error: $e")
            }
        }
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

    /**
     * Method that retrieves a Picture of the Day from the network
     */
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
