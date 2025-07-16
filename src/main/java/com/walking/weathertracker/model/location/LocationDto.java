package com.walking.weathertracker.model.location;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class LocationDto {
    private Long id;
    private String name;
    private String state;
    private String description;
    private String icon;
    private double temperature;
    private double feelsLike;
    private double temperatureMin;
    private double temperatureMax;
    private int pressure;
    private int visibility;
    private int humidity;
    private double windSpeed;
    private double latitude;
    private double longitude;
    private LocalTime sunriseTime;
    private LocalTime sunsetTime;
}
