package com.walking.weathertracker.converter.location;

import com.walking.weathertracker.model.location.LocationDto;
import com.walking.weathertracker.model.weather.WeatherResponse;
import com.walking.weathertracker.util.DateTimeUtils;
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
        target.setTemperatureMin(source.getMain().getTempMin());
        target.setTemperatureMax(source.getMain().getTempMax());
        target.setPressure(source.getMain().getPressure());
        target.setVisibility(source.getVisibility());
        target.setHumidity(source.getMain().getHumidity());
        target.setLatitude(source.getCoord().getLatitude());
        target.setWindSpeed(source.getWind().getSpeed());
        target.setLongitude(source.getCoord().getLongitude());

        if (!source.getWeather().isEmpty()) {
            target.setDescription(formatDescription(source.getWeather().getFirst().getDescription()));
            target.setIcon(source.getWeather().getFirst().getIcon());
        }

        target.setSunriseTime(DateTimeUtils.parseLocalTimeFromSeconds(source.getSys().getSunrise()));
        target.setSunsetTime(DateTimeUtils.parseLocalTimeFromSeconds(source.getSys().getSunset()));

        return target;
    }

    private String formatDescription(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}
