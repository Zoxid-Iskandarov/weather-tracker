package com.walking.weathertracker.converter.location;

import com.walking.weathertracker.model.location.ForecastDto;
import com.walking.weathertracker.model.weather.ForecastResponse;
import com.walking.weathertracker.util.DateTimeUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ForecastDtoConverter implements Converter<ForecastResponse.WeatherForecast, ForecastDto> {
    @Override
    public ForecastDto convert(ForecastResponse.WeatherForecast source) {
        var target = new ForecastDto();

        target.setTemperature(source.getMain().getTemperature());
        target.setFeelsLike(source.getMain().getTemperatureFeelsLike());
        target.setHumidity(source.getMain().getHumidity());
        target.setDateTime(DateTimeUtils.parseToLocalDateTime(source.getDateTime()));
        target.setWindSpeed(source.getWind().getSpeed());

        if (!source.getWeather().isEmpty()) {
            target.setMain(source.getWeather().getFirst().getMain());
            target.setDescription(source.getWeather().getFirst().getDescription());
            target.setIcon(source.getWeather().getFirst().getIcon());
        }

        return target;
    }
}
