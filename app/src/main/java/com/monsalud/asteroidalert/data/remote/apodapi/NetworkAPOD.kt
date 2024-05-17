package com.monsalud.asteroidalert.data.remote.apodapi

/**
 * Data class to receive Astronomy Picture of the Day JSON objects
 */
data class NetworkAPOD(
    val copyright: String,
    val date: String,
    val explanation: String,
    val hdurl: String,
    val media_type: String,
    val service_version: String,
    val title: String,
    val url: String
)