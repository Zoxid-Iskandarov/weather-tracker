package com.walking.weathertracker.controller;

import com.walking.weathertracker.config.security.CustomUserDetails;
import com.walking.weathertracker.model.location.ForecastDto;
import com.walking.weathertracker.model.location.LocationDto;
import com.walking.weathertracker.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class LocationController {
    private final LocationService locationService;

    @GetMapping("/")
    public String index(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        model.addAttribute("locations", locationService.getLocations(userDetails.getId()));
        model.addAttribute("username", userDetails.getUsername());

        return "index";
    }

    @GetMapping("/search")
    public String search(@RequestParam("location") String location,
                         @AuthenticationPrincipal CustomUserDetails userDetails,
                         Model model) {
        if (location == null || location.isBlank()) {
            model.addAttribute("username", userDetails.getUsername());
            model.addAttribute("error", "Location cannot be empty");
            return "index";
        }

        model.addAttribute("username", userDetails.getUsername());
        model.addAttribute("locations", locationService.getLocationsByCity(location));

        return "search-results";
    }

    @PostMapping("/add-location")
    public String addLocation(@RequestParam("latitude") double latitude,
                              @RequestParam("longitude") double longitude,
                              @AuthenticationPrincipal CustomUserDetails userDetails) {
        locationService.save(latitude, longitude, userDetails.getId());

        return "redirect:/";
    }

    @DeleteMapping("/delete-location/{id}")
    public String deleteLocation(@PathVariable("id") Long id) {
        locationService.deleteById(id);

        return "redirect:/";
    }

    @PostMapping("/forecast")
    @PreAuthorize("@locationSecurity.isOwner(#locationDto.id, #userDetails.id)")
    public String getForecast(@ModelAttribute LocationDto locationDto,
                              @AuthenticationPrincipal CustomUserDetails userDetails,
                              Model model) {
        List<ForecastDto> forecasts = locationService
                .getForecasts(locationDto.getLatitude(), locationDto.getLongitude());

        model.addAttribute("currentLocation", locationDto);
        model.addAttribute("hourlyForecasts", locationService.getHourlyForecasts(forecasts));
        model.addAttribute("dailyForecasts", locationService.getDailyForecasts(forecasts));
        model.addAttribute("username", userDetails.getUsername());

        return "forecast-view";
    }
}
