package com.example.weather.data;

import java.util.Objects;

/**
 * Data model for API Geocoding response. Contains pertinent information such as the city name and country.
 */
public class GeocodingResult {
    private String name;
    private float lat;
    private float lon;
    private String country;

    public GeocodingResult(String name, int lat, int lon, String country) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeocodingResult that = (GeocodingResult) o;
        return lat == that.lat && lon == that.lon && Objects.equals(name, that.name) && Objects.equals(country, that.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, lat, lon, country);
    }

    @Override
    public String toString() {
        return "GeocodingResult{" +
                "name='" + name + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                ", country='" + country + '\'' +
                '}';
    }
}
