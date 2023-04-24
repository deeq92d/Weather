package com.example.weather.api;

import com.example.weather.Constants;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Builder for the {@link Retrofit} service which is used to communicate with the weather API.
 */
public class WeatherServiceBuilder {
    private OkHttpClient client = new OkHttpClient.Builder().build();
    private Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.WEATHER_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client).build();

    public <T> T buildService(Class<T> service) {
        return retrofit.create(service);
    }
}
