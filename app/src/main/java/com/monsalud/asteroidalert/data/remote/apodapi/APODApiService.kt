package com.monsalud.asteroidalert.data.remote.apodapi

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.monsalud.asteroidalert.data.remote.Constants
import com.monsalud.asteroidalert.data.remote.neowsapi.NetworkAsteroidContainer
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(Constants.BASE_URL)
    .build()

/**
 * API Service to make the Astronomy Picture of the Day Service calls
 */
interface APODApiService {
    @GET("planetary/apod")
    fun getApod(
        @Query("api_key") apiKey: String,
        @Query("thumbs") returnThumbnail: Boolean
    ) : Deferred<NetworkAPOD>
}

object APODApi {
    val retrofitService: APODApiService by lazy {
        retrofit.create(APODApiService::class.java)
    }
}