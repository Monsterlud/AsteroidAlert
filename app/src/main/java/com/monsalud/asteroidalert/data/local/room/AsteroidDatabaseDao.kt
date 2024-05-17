package com.monsalud.asteroidalert.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.monsalud.asteroidalert.domain.Asteroid
import retrofit2.http.GET
import kotlin.reflect.KClass

@Dao
interface AsteroidDatabaseDao {

    @Query("SELECT * FROM incoming_asteroid_table")
    fun getAllAsteroids(): LiveData<List<AsteroidEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllAsteroids(vararg asteroid: AsteroidEntity)

    @Query("DELETE FROM incoming_asteroid_table")
    fun clearDatabase()
}