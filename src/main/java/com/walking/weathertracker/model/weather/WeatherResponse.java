package com.walking.weathertracker.model.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.walking.weathertracker.model.weather.entity.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResponse {
    private Coord coord;

    private List<Weather> weather;

    private Main main;

    private Sys sys;

    private String name;

    private Wind wind;

    private Integer visibility;
}
