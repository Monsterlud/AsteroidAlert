package com.monsalud.asteroidalert.presentation.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.monsalud.asteroidalert.domain.Asteroid

class DetailViewModel(
    asteroid: Asteroid,
    application: Application
) : AndroidViewModel(application) {

    /**
     * LiveData to update which Asteroid is passed to Detail screen
     */
    private val _selectedAsteroid = MutableLiveData<Asteroid>()
    val selectedAsteroid: LiveData<Asteroid>
        get() = _selectedAsteroid

    init {
        _selectedAsteroid.value = asteroid
    }
}