package com.monsalud.asteroidalert.presentation.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.monsalud.asteroidalert.data.AsteroidRepository
import com.monsalud.asteroidalert.data.remote.apodapi.NetworkAPOD
import com.monsalud.asteroidalert.domain.Asteroid
import kotlinx.coroutines.launch

enum class AsteroidApiStatus { LOADING, ERROR, DONE }

class MainViewModel(
    private val repository: AsteroidRepository
) : ViewModel() {

    /**
     * LiveData holding the latest NetworkAPOD object for the Fragment
     * to extract the url as well as the explanation of the image
     */
    private val _pictureOfTheDay = MutableLiveData<NetworkAPOD?>()
    val pictureOfTheDay: LiveData<NetworkAPOD?>
        get() = _pictureOfTheDay

    /**
     * LiveData holding the list of Asteroids
     */
    val asteroids: LiveData<List<Asteroid>>
        get() = repository.asteroids

    /**
     * LiveData that tells the Fragment when to navigate to the AsteroidDetail screen
     */
    private val _navigateToAsteroidDetail = MutableLiveData<Asteroid?>()
    val navigateToAsteroidDetail: LiveData<Asteroid?>
        get() = _navigateToAsteroidDetail

    /**
     * LiveData that tells the Fragment what the current loading status is for the Api call
     */
    private val _loadingStatus = MutableLiveData<AsteroidApiStatus>()
    val loadingStatus: LiveData<AsteroidApiStatus>
        get() = _loadingStatus

    init {
        viewModelScope.launch {
            updatePictureOfTheDay()
            refreshAsteroids()
        }
    }

    private suspend fun refreshAsteroids() {
        _loadingStatus.value = AsteroidApiStatus.LOADING
        try {
            repository.refreshAsteroids()
            _loadingStatus.value = AsteroidApiStatus.DONE
        } catch (e: Exception) {
            _loadingStatus.value = AsteroidApiStatus.ERROR
            Log.e("MainViewModel", "refreshAsteroids error: ${e.message}", )
        }
    }

    private suspend fun updatePictureOfTheDay() {
        val apod = repository.getApod()
        _pictureOfTheDay.value = apod
    }

    suspend fun clearAsteroids() {
        repository.clearAsteroids()
    }

    /**
     * Navigation to AsteroidDetail screen methods
     */
    fun onAsteroidItemClicked(asteroid: Asteroid) {
        _navigateToAsteroidDetail.value = asteroid
    }

    fun doneNavigating() {
        _navigateToAsteroidDetail.value = null
    }
}
