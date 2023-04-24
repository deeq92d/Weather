package com.example.weather;

import static androidx.core.content.PermissionChecker.PERMISSION_GRANTED;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.weather.databinding.ActivityMainBinding;
import com.example.weather.viewmodel.MainViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

/**
 * Main activity for the application, business logic is delegated to the {@link MainViewModel}.
 * <p>
 * Note: Would have liked to use more RxJava/Coroutines however the only asynchronous operations (API communication) are handled asynchronously by Retrofit.
 */
public class MainActivity extends AppCompatActivity {
    private static int LOCATION_PERMISSION_REQUEST_CODE = 111;
    private static String LAST_SEARCHED_LOCATION_KEY = "last_searched_location";

    private ActivityMainBinding binding;
    private MainViewModel viewModel;
    private FusedLocationProviderClient locationProviderClient;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getApplication().getSharedPreferences("application", Context.MODE_PRIVATE);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        binding.setLifecycleOwner(this);
        binding.weatherInformation.setVm(viewModel);

        binding.searchbar.setQuery(sharedPreferences.getString(LAST_SEARCHED_LOCATION_KEY, ""), false);
        //small workaround - clear focus so that keyboard does not display when setting previous search query
        binding.searchbar.clearFocus();
        setContentView(binding.getRoot());

        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getLocationAndSearch();
        }

        binding.searchbar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (s != null && !s.isEmpty()) {
                    sharedPreferences.edit().putString(LAST_SEARCHED_LOCATION_KEY, s).commit();

                    binding.progressBar.setVisibility(View.VISIBLE);
                    binding.searchbar.clearFocus();
                    viewModel.search(s);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        viewModel.getApiResponse().observe(this, api_response -> {
            binding.progressBar.setVisibility(View.GONE);

            switch (api_response) {
                case CITY_NOT_FOUND:
                    Snackbar.make(binding.searchbar, "Error occurred fetching the city", Snackbar.LENGTH_SHORT).show();
                    break;
                case ERROR_FETCHING_WEATHER:
                    Snackbar.make(binding.searchbar, "Error occurred fetching the weather", Snackbar.LENGTH_SHORT).show();
                    break;
                case ERROR:
                    Snackbar.make(binding.searchbar, "Error communicating with API", Snackbar.LENGTH_SHORT).show();
                    break;
            }
        });

        viewModel.getWeatherResult().observe(this, weatherResult -> {
            Picasso.get().load(String.format("https://openweathermap.org/img/wn/%s@2x.png", weatherResult.getWeather()[0].getIcon())).into(binding.weatherInformation.weatherIcon);
        });
    }

    @SuppressLint("MissingPermission")
    private void getLocationAndSearch() {
        locationProviderClient.getLastLocation().addOnCompleteListener(task -> {
            if (task.getResult() != null) {
                double longitude = task.getResult().getLongitude();
                double latitude = task.getResult().getLatitude();
                viewModel.search(latitude, longitude);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PERMISSION_GRANTED) {
                getLocationAndSearch();
            }
        }
    }
}