package com.walking.weathertracker.service;

import com.walking.weathertracker.converter.location.ForecastDtoConverter;
import com.walking.weathertracker.converter.location.LocationDtoConverter;
import com.walking.weathertracker.converter.location.WeatherResponseConverter;
import com.walking.weathertracker.model.location.ForecastDto;
import com.walking.weathertracker.model.location.LocationDto;
import com.walking.weathertracker.model.weather.LocationResponse;
import com.walking.weathertracker.repository.LocationRepository;
import com.walking.weathertracker.repository.UserRepository;
import com.walking.weathertracker.util.DateTimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LocationService {
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;
    private final WeatherApiService weatherApiService;
    private final LocationDtoConverter locationDtoConverter;
    private final ForecastDtoConverter forecastDtoConverter;
    private final WeatherResponseConverter weatherResponseConverter;

    public List<LocationDto> getLocations(Long userId) {
        return locationRepository.findByUserIdOrderByCreatedAt(userId)
                .stream()
                .map(location -> {
                    var weatherResponse = weatherApiService
                            .getWeatherByCoordinate(location.getLatitude(), location.getLongitude());

                    var locationDto = locationDtoConverter.convert(weatherResponse);
                    Objects.requireNonNull(locationDto).setId(location.getId());

                    return locationDto;
                })
                .toList();
    }

    public List<LocationResponse> getLocationsByCity(String city) {
        return weatherApiService.getLocations(city);
    }

    public List<ForecastDto> getForecasts(double latitude, double longitude) {
        return weatherApiService.getForecasts(latitude, longitude)
                .getWeatherForecasts().stream()
                .map(forecastDtoConverter::convert)
                .toList();
    }

    public List<ForecastDto> getHourlyForecasts(List<ForecastDto> forecasts) {
        return forecasts.stream()
                .limit(8)
                .toList();
    }

    public List<ForecastDto> getDailyForecasts(List<ForecastDto> forecasts) {
        var dailyForecasts = new HashMap<String, ForecastDto>();

        for (ForecastDto forecast : forecasts) {
            String dateTime = DateTimeUtils.formatToString(forecast.getDateTime());
            String date = dateTime.split(" ")[0];
            String time = dateTime.split(" ")[1];

            if (!dailyForecasts.containsKey(date) || time.equals("12:00:00")) {
                dailyForecasts.put(date, forecast);
            }
        }

        return dailyForecasts.values()
                .stream()
                .sorted(Comparator.comparing(ForecastDto::getDateTime))
                .limit(5L)
                .toList();
    }

    @Transactional
    public void save(double latitude, double longitude, long userId) {
        var weatherResponse = weatherApiService.getWeatherByCoordinate(latitude, longitude);

        if (locationRepository.existsByUserIdAndName(userId, weatherResponse.getName())) {
            return;
        }

        Optional.ofNullable(weatherResponseConverter.convert(weatherResponse))
                .ifPresent(location -> {
                    userRepository.findById(userId)
                            .ifPresent(location::setUser);

                    locationRepository.save(location);
                });
    }

    @Transactional
    @PreAuthorize("@locationSecurity.isOwner(#locationId, principal.id)")
    public void deleteById(Long locationId) {
        locationRepository.findById(locationId)
                .ifPresent(location -> {
                    locationRepository.delete(location);
                    locationRepository.flush();
                });
    }
}
