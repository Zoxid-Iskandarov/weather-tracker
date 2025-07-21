package com.walking.weathertracker.integration.controller;

import com.walking.weathertracker.integration.IntegrationTestBase;
import com.walking.weathertracker.model.location.LocationDto;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@RequiredArgsConstructor
public class LocationControllerIT extends IntegrationTestBase {
    private final MockMvc mockMvc;

    @Test
    void getMainPage_returnViewWithAttributes() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("locations"))
                .andExpect(model().attributeExists("username"))
                .andExpect(view().name("index"));
    }

    @Test
    void search_whenValidLocation() throws Exception {
        mockMvc.perform(get("/search")
                        .queryParam("location", "London"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("username"))
                .andExpect(model().attributeExists("locations"))
                .andExpect(view().name("search-results"));
    }

    @Test
    void search_whenInvalidLocation() throws Exception {
        mockMvc.perform(get("/search")
                        .queryParam("location", ""))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("username"))
                .andExpect(model().attributeExists("error"))
                .andExpect(view().name("index"));
    }

    @Test
    void addLocation_redirectToIndex() throws Exception {
        mockMvc.perform(post("/add-location")
                        .param("latitude", "51.5074")
                        .param("longitude", "-0.1278")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void deleteLocation_redirectToIndex() throws Exception {
        mockMvc.perform(delete("/delete-location/{id}", 1L)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void getForecast_whenAuthorized_returnsForecastView() throws Exception {
        mockMvc.perform(post("/forecast")
                        .flashAttr("locationDto", getLocationDto())
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("forecast-view"))
                .andExpect(model().attributeExists("currentLocation"))
                .andExpect(model().attributeExists("hourlyForecasts"))
                .andExpect(model().attributeExists("dailyForecasts"))
                .andExpect(model().attributeExists("username"));
    }

    private LocationDto getLocationDto() {
        LocationDto dto = new LocationDto();
        dto.setId(1L);
        dto.setName("London");
        dto.setState("England");
        dto.setDescription("Cloudy");
        dto.setIcon("04d");
        dto.setTemperature(15.5);
        dto.setFeelsLike(14.0);
        dto.setTemperatureMin(13.0);
        dto.setTemperatureMax(17.0);
        dto.setPressure(1012);
        dto.setVisibility(10000);
        dto.setHumidity(80);
        dto.setWindSpeed(4.5);
        dto.setLatitude(51.5074);
        dto.setLongitude(-0.1278);
        dto.setSunriseTime(LocalTime.of(5, 30));
        dto.setSunsetTime(LocalTime.of(20, 45));

        return dto;
    }
}
