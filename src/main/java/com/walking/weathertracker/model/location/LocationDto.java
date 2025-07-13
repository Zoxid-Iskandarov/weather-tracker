package com.walking.weathertracker.model.location;

import lombok.Getter;
import lombok.Setter;

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
    private int humidity;
}
