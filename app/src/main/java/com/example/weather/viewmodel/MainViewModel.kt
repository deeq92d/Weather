package com.example.weather.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.Constants
import com.example.weather.api.WeatherEndpoints
import com.example.weather.api.WeatherServiceBuilder
import com.example.weather.data.GeocodingResult
import com.example.weather.data.WeatherResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * The main view model for the [MainActivity]. Handles all business logic including API communication.
 */
class MainViewModel() : ViewModel() {
    private var _weatherResult: MutableLiveData<WeatherResult> = MutableLiveData()
    var weatherResult: LiveData<WeatherResult> = _weatherResult

    private var _apiResponse: MutableLiveData<API_RESPONSE> = MutableLiveData()
    var apiResponse: LiveData<API_RESPONSE> = _apiResponse

    val service = WeatherServiceBuilder().buildService(WeatherEndpoints::class.java)

    /**
     * Initially search for coordinates to a location followed by another API call to fetch the weather data based off the coordinates.
     */
    fun search(location: String) {
        val geocodingCall = service.getCoordinates(location, Constants.WEATHER_API_KEY)
        geocodingCall.enqueue(object : Callback<Array<GeocodingResult>> {
            override fun onResponse(
                call: Call<Array<GeocodingResult>>, response: Response<Array<GeocodingResult>>
            ) {
                if (response.body()!!.isNotEmpty()) {
                    //In the interest of time we are grabbing the first result, this can cause several issues
                    // and ideally we would display a list of cities for the user
                    search(response.body()!![0].lat.toDouble(), response.body()!![0].lon.toDouble())
                } else {
                    onFailure(call, Throwable("City not found"))
                    _apiResponse.postValue(API_RESPONSE.CITY_NOT_FOUND)
                }
            }

            override fun onFailure(call: Call<Array<GeocodingResult>>, throwable: Throwable) {
                Log.e("API", "error fetching geocoding coordinates: $throwable")
                _apiResponse.postValue(API_RESPONSE.ERROR)
            }
        })
    }

    /**
     * Search for weather data given a set of latitude/longitude coordinates.
     */
    fun search(lat: Double, lon: Double) {
        val call = service.getWeather(
            Constants.WEATHER_API_KEY, lat.toString(), lon.toString(), "imperial"
        )

        call.enqueue(object : Callback<WeatherResult> {
            override fun onResponse(
                p0: Call<WeatherResult>, response: Response<WeatherResult>
            ) {
                if (response.body() != null) _weatherResult.postValue(response.body())

                Log.i("API", "successfully fetched weather: " + response.body()!!.toString())
                _apiResponse.postValue(API_RESPONSE.OK)
            }

            override fun onFailure(p0: Call<WeatherResult>, throwable: Throwable) {
                Log.e("API", "error fetching weather: $throwable")
                _apiResponse.postValue(API_RESPONSE.ERROR_FETCHING_WEATHER)
            }
        })
    }
}

/**
 * Enums representing the status of the API responses
 */
enum class API_RESPONSE {
    OK, CITY_NOT_FOUND, ERROR_FETCHING_WEATHER, ERROR
}