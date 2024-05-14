package com.monsalud.asteroidalert.presentation.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.monsalud.asteroidalert.data.AsteroidRepository
import com.monsalud.asteroidalert.data.local.room.AsteroidDatabaseDao
import com.monsalud.asteroidalert.data.local.room.AsteroidEntity
import com.monsalud.asteroidalert.domain.Asteroid
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: AsteroidRepository
) : ViewModel() {

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

    init {
        viewModelScope.launch {
            repository.refreshAsteroids()
        }
    }

    suspend fun getAsteroidFromId(id: Long) : Asteroid {
        return repository.getAsteroid(id)
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
