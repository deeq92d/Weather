package com.example.weather.api;

import com.example.weather.data.GeocodingResult;
import com.example.weather.data.WeatherResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Endpoint interface for use with the {@link WeatherServiceBuilder}.
 */
public interface WeatherEndpoints {
    /**
     * API Call to get weather information for a set of coordinates.
     *
     * @param apiKey The API key - A non-null/empty String.
     * @param lat    The latitude - A non-null/empty String.
     * @param lon    The longitude - A non-null/empty String.
     * @param unit   The unit type desired for response; i.e. Standard, Metric, Imperial - A non-null/empty String.
     * @return A {@link Call} containing the {@link WeatherResult}.
     */
    @GET("/data/2.5/weather")
    public Call<WeatherResult> getWeather(@Query("appid") String apiKey, @Query("lat") String lat, @Query("lon") String lon, @Query("units") String unit);

    @GET("geo/1.0/direct")
    public Call<GeocodingResult[]> getCoordinates(@Query("q") String locationName, @Query("appid") String apiKey);
}
