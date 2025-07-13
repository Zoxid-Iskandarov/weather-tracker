package com.walking.weathertracker.controller;

import com.walking.weathertracker.config.security.CustomUserDetails;
import com.walking.weathertracker.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

        return "search-result";
    }

    @PostMapping("/add-location")
    public String addLocation(@RequestParam("latitude") double latitude,
                              @RequestParam("longitude") double longitude,
                              @AuthenticationPrincipal CustomUserDetails userDetails) {
        locationService.save(latitude, longitude, userDetails.getId());

        return "redirect:/";
    }

    @DeleteMapping("/delete-location/{id}")
    private String deleteLocation(@PathVariable("id") Long id) {
        locationService.deleteById(id);

        return "redirect:/";
    }
}
