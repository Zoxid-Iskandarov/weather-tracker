package com.walking.weathertracker.model.location;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ForecastDto {
    private double temperature;
    private double feelsLike;
    private int humidity;
    private String main;
    private String description;
    private String icon;
    private double windSpeed;
    private LocalDateTime dateTime;
}
