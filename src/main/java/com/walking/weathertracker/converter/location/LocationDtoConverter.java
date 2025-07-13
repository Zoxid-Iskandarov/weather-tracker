package com.walking.weathertracker.converter.location;

import com.walking.weathertracker.model.location.LocationDto;
import com.walking.weathertracker.model.weather.WeatherResponse;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class LocationDtoConverter implements Converter<WeatherResponse, LocationDto> {
    @Override
    public LocationDto convert(WeatherResponse source) {
        var target = new LocationDto();

        target.setName(source.getName());
        target.setState(source.getSys().getCountry());
        target.setTemperature(source.getMain().getTemperature());
        target.setFeelsLike(source.getMain().getTemperatureFeelsLike());
        target.setHumidity(source.getMain().getHumidity());

        if (!source.getWeather().isEmpty()) {
            target.setDescription(formatDescription(source.getWeather().getFirst().getDescription()));
            target.setIcon(source.getWeather().getFirst().getIcon());
        }

        return target;
    }

    private String formatDescription(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}
