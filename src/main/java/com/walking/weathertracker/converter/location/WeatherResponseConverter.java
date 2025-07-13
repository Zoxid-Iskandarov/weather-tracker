package com.walking.weathertracker.converter.location;

import com.walking.weathertracker.domain.Location;
import com.walking.weathertracker.model.weather.WeatherResponse;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class WeatherResponseConverter implements Converter<WeatherResponse, Location> {
    @Override
    public Location convert(WeatherResponse source) {
        Location location = new Location();

        location.setName(source.getName());
        location.setLatitude(source.getCoord().getLatitude());
        location.setLongitude(source.getCoord().getLongitude());

        return location;
    }
}
