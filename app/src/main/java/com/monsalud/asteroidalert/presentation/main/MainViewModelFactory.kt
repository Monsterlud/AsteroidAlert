package com.monsalud.asteroidalert.presentation.main

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.monsalud.asteroidalert.data.AsteroidRepository
import com.monsalud.asteroidalert.data.local.room.AsteroidDatabaseDao

class MainViewModelFactory(
    private val repository: AsteroidRepository
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}