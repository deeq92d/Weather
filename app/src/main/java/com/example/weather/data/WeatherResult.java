package com.example.weather.data;

import java.util.Objects;

/**
 * Data model for the main JSON result when calling the weather API.
 */
public class WeatherResult {
    private Coord coord;
    private WeatherGeneral[] weather;
    private WeatherInformation main;
    //city name
    private String name;

    public WeatherResult(Coord coord, WeatherGeneral[] weatherGeneral, WeatherInformation main, String name) {
        this.coord = coord;
        this.weather = weatherGeneral;
        this.main = main;
        this.name = name;
    }

    public Coord getCoord() {
        return coord;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public WeatherGeneral[] getWeather() {
        return weather;
    }

    public void setWeather(WeatherGeneral[] weather) {
        this.weather = weather;
    }

    public WeatherInformation getMain() {
        return main;
    }

    public void setMain(WeatherInformation main) {
        this.main = main;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WeatherResult that = (WeatherResult) o;
        return Objects.equals(coord, that.coord) && Objects.equals(weather, that.weather) && Objects.equals(main, that.main) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coord, weather, main, name);
    }

    @Override
    public String toString() {
        return "WeatherResult{" +
                "coord=" + coord +
                ", weatherGeneral=" + weather +
                ", main=" + main +
                ", name='" + name + '\'' +
                '}';
    }
}
