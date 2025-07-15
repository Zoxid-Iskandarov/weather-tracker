package com.walking.weathertracker.model.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.walking.weathertracker.model.weather.entity.Main;
import com.walking.weathertracker.model.weather.entity.Weather;
import com.walking.weathertracker.model.weather.entity.Wind;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ForecastResponse {
    @JsonProperty("list")
    private List<WeatherForecast> weatherForecasts = new ArrayList<>();

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class WeatherForecast {
        private Main main;

        private List<Weather> weather;

        private Wind wind;

        @JsonProperty("dt_txt")
        private String dateTime;
    }
}
