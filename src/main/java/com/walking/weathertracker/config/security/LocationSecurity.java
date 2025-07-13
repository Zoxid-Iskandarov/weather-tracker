package com.walking.weathertracker.config.security;

import com.walking.weathertracker.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LocationSecurity {
    private final LocationRepository locationRepository;

    public boolean isOwner(Long locationId, Long userId) {
        return locationRepository.findById(locationId)
                .map(location -> location.getUser().getId().equals(userId))
                .orElse(false);
    }
}
