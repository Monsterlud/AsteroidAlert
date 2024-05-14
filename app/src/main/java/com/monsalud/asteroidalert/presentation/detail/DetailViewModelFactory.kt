package com.monsalud.asteroidalert.presentation.detail

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.monsalud.asteroidalert.domain.Asteroid

class DetailViewModelFactory(
    private val asteroid: Asteroid,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(asteroid, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}