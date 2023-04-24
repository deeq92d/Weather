package com.example.weather.data;

import java.util.Objects;

/**
 * Data model for a general weather description & icon ID for display purposes
 */
public class WeatherGeneral {
    private int id;
    private String main;
    private String description;
    private String icon;

    public WeatherGeneral(int id, String main, String description, String icon) {
        this.id = id;
        this.main = main;
        this.description = description;
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WeatherGeneral weatherGeneral = (WeatherGeneral) o;
        return Objects.equals(id, weatherGeneral.id) && Objects.equals(main, weatherGeneral.main) && Objects.equals(description, weatherGeneral.description) && Objects.equals(icon, weatherGeneral.icon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, main, description, icon);
    }

    @Override
    public String toString() {
        return "Weather{" + "id='" + id + '\'' + ", main='" + main + '\'' + ", description='" + description + '\'' + ", icon='" + icon + '\'' + '}';
    }
}
