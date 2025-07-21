package com.walking.weathertracker.integration.service;

import com.walking.weathertracker.integration.IntegrationTestBase;
import com.walking.weathertracker.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
public class LocationServiceIT extends IntegrationTestBase {
    private static final Long USER_ID = 1L;
    private static final Long OTHER_USER_ID = 3L;
    private static final String LONDON = "London";
    private static final double LONDON_LAT = 51.5074;
    private static final double LONDON_LON = -0.1278;

    private final LocationService locationService;

    @Test
    void getLocations_whenUserHasLocations_success() {
        var actual = locationService.getLocations(USER_ID);

        assertThat(actual).isNotNull();
        assertThat(actual).hasSize(3);
    }

    @Test
    void getLocations_whenUserHasNotLocations_success() {
        var actual = locationService.getLocations(OTHER_USER_ID);

        assertThat(actual).isEmpty();
    }

    @Test
    void getLocationsByCity_whenValidParam_success() {
        var actual = locationService.getLocationsByCity(LONDON);

        assertThat(actual).isNotEmpty();
    }

    @Test
    void getLocationsByCity_whenIsNotValidParam_success() {
        var actual = locationService.getLocationsByCity("0123456789");

        assertThat(actual).isEmpty();
    }

    @Test
    void getForecasts_success() {
        var actual = locationService.getForecasts(LONDON_LAT, LONDON_LON);

        assertThat(actual).isNotEmpty();
    }

    @Test
    void getForecasts_whenIsNotValidParam_failed() {
        assertThrows(Exception.class, () -> locationService.getForecasts(200, -200));
    }

    @Test
    void getHourlyForecasts_success() {
        var actual = locationService.getHourlyForecasts(locationService.getForecasts(LONDON_LAT, LONDON_LON));

        assertThat(actual).isNotEmpty();
        assertThat(actual).hasSize(8);
        assertEquals(actual.getFirst().getDateTime(), actual.getLast().getDateTime().minusHours(21));
    }

    @Test
    void getDailyForecasts_success() {
        var actual = locationService.getDailyForecasts(locationService.getForecasts(LONDON_LAT, LONDON_LON));

        assertThat(actual).isNotEmpty();
        assertThat(actual).hasSize(5);
        assertEquals(actual.get(1).getDateTime(), actual.getLast().getDateTime().minusDays(3));
    }

    @Test
    void save_success() {
        locationService.save(LONDON_LAT, LONDON_LON, OTHER_USER_ID);

        var actual = locationService.getLocations(OTHER_USER_ID);

        assertThat(actual).isNotEmpty();
        assertThat(actual).hasSize(1);
    }

    @Test
    void save_whenLocationAlreadyExists_skipSave() {
        locationService.save(LONDON_LAT, LONDON_LON, USER_ID);

        var actual = locationService.getLocations(USER_ID);

        assertThat(actual).isNotEmpty();
        assertThat(actual).hasSize(3);
    }
}
