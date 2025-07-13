package com.walking.weathertracker.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.walking.weathertracker.model.weather.LocationResponse;
import com.walking.weathertracker.model.weather.WeatherResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Component
@RequiredArgsConstructor
public class WeatherApiService {
    private static final String APP_ID = "YOUR_API_KEY_HERE"; // TODO: Replace with your API key
    private static final String BASE_URL = "https://api.openweathermap.org";
    private static final String WEATHER_API_SUFFIX = "/data/2.5/weather";
    private static final String GEOCODING_API_SUFFIX = "/geo/1.0/direct";

    private final WebClient client;
    private final ObjectMapper objectMapper;

    public WeatherResponse getWeatherByCoordinate(double latitude, double longitude) {
        URI uri = buildUrlForWeather(latitude, longitude);

        return client.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(WeatherResponse.class)
                .block();
    }

    @SneakyThrows
    public List<LocationResponse> getLocations(String locationName) {
        URI uri = buildUrlForLocation(locationName);

        String json = client.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return objectMapper.readValue(json, new TypeReference<>() {});
    }

    private URI buildUrlForWeather(double latitude, double longitude) {
        return UriComponentsBuilder
                .fromUriString(BASE_URL + WEATHER_API_SUFFIX)
                .queryParam("lat", latitude)
                .queryParam("lon", longitude)
                .queryParam("appid", APP_ID)
                .queryParam("units", "metric")
                .build()
                .toUri();
    }

    private URI buildUrlForLocation(String location) {
        return UriComponentsBuilder
                .fromUriString(BASE_URL + GEOCODING_API_SUFFIX)
                .queryParam("q", location)
                .queryParam("limit", 5)
                .queryParam("appid", APP_ID)
                .build()
                .toUri();
    }
}
