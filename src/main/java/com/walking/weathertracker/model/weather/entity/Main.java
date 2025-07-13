package com.walking.weathertracker.model.weather.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Main {
    @JsonProperty("temp")
    private Double temperature;

    @JsonProperty("feels_like")
    private Double temperatureFeelsLike;

    private Integer humidity;
}
