package com.monsalud.asteroidalert.data.remote

import android.util.Log
import com.monsalud.asteroidalert.domain.Asteroid
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

private const val DATE_FORMAT = "%04d-%02d-%02d"

fun parseAsteroidsJsonResult(jsonResult: JSONObject): ArrayList<Asteroid> {
    val nearEarthObjectsJson = jsonResult.getJSONObject("near_earth_objects")

    val asteroidList = ArrayList<Asteroid>()

    val nextSevenDaysFormattedDates = getNextSevenDaysFormattedDates()
    for (formattedDate in nextSevenDaysFormattedDates) {
        if (nearEarthObjectsJson.has(formattedDate)) {
            val dateAsteroidJsonArray = nearEarthObjectsJson.getJSONArray(formattedDate)

            for (i in 0 until dateAsteroidJsonArray.length()) {
                val asteroidJson = dateAsteroidJsonArray.getJSONObject(i)
                val id = asteroidJson.getLong("id")
                val codename = asteroidJson.getString("name")
                val absoluteMagnitude = asteroidJson.getDouble("absolute_magnitude_h")
                val estimatedDiameter = asteroidJson.getJSONObject("estimated_diameter")
                    .getJSONObject("kilometers").getDouble("estimated_diameter_max")

                val closeApproachData = asteroidJson
                    .getJSONArray("close_approach_data").getJSONObject(0)
                val relativeVelocity = closeApproachData.getJSONObject("relative_velocity")
                    .getDouble("kilometers_per_second")
                val distanceFromEarth = closeApproachData.getJSONObject("miss_distance")
                    .getDouble("astronomical")
                val isPotentiallyHazardous = asteroidJson
                    .getBoolean("is_potentially_hazardous_asteroid")

                val asteroid = Asteroid(
                    id, codename,
                    formattedDate,
                    absoluteMagnitude,
                    estimatedDiameter,
                    relativeVelocity,
                    distanceFromEarth,
                    isPotentiallyHazardous
                )
                asteroidList.add(asteroid)
            }
        }
    }

    return asteroidList
}

private fun getNextSevenDaysFormattedDates(): ArrayList<String> {
    val formattedDateList = ArrayList<String>()

    val calendar = Calendar.getInstance()
    for (i in 0..Constants.DEFAULT_END_DATE_DAYS) {
        val currentTime = calendar.time
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        formattedDateList.add(dateFormat.format(currentTime))
        calendar.add(Calendar.DAY_OF_YEAR, 1)
    }

    return formattedDateList
}

fun getTodayDate(): String {
    val calendar = Calendar.getInstance()
    return getFormattedDate(calendar)
}

fun getEndSearchDate(): String {
    val calendar = Calendar.getInstance()
    calendar.timeZone = TimeZone.getDefault()
    calendar.add(Calendar.DAY_OF_MONTH, 6)
    return getFormattedDate(calendar)
}

/**
 * Helper method that returns the Saturday date of the current week
 * This allows the app to filter the list of Asteroids to those that are in close approach during the remainder of this week
 */
fun getEndOfWeekDate(calendar: Calendar): String {
    val today = calendar.get(Calendar.DAY_OF_WEEK)
    Log.i("network utils", "todayInt: $today")
    val numberOfDaysLeftInWeek = if (today == Calendar.SATURDAY) 7 else 7 - today
    Log.i("network utils", "numberOfDaysLeftInWeek: $numberOfDaysLeftInWeek")
    calendar.add(Calendar.DAY_OF_YEAR, numberOfDaysLeftInWeek)
    Log.i("network utils", "newDate: ${getFormattedDate(calendar)}")
    return getFormattedDate(calendar)
}

private fun getFormattedDate(calendar: Calendar): String {
    return String.format(
        Locale.getDefault(),
        DATE_FORMAT,
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH) + 1,
        calendar.get(Calendar.DAY_OF_MONTH)
    )
}
