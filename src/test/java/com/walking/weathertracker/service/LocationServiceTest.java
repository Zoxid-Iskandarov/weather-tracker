package com.walking.weathertracker.service;

import com.walking.weathertracker.converter.location.LocationDtoConverter;
import com.walking.weathertracker.converter.location.WeatherResponseConverter;
import com.walking.weathertracker.domain.Location;
import com.walking.weathertracker.domain.User;
import com.walking.weathertracker.model.location.LocationDto;
import com.walking.weathertracker.model.weather.LocationResponse;
import com.walking.weathertracker.model.weather.WeatherResponse;
import com.walking.weathertracker.repository.LocationRepository;
import com.walking.weathertracker.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class LocationServiceTest {
    private static final Long USER_ID = 1L;
    private static final Long NEW_YORK_ID = 1L;
    private static final Long LONDON_ID = 2L;

    private static final String NEW_YORK = "New York";
    private static final String LONDON = "London";

    private static final double NEW_YORK_LAT = 40.7128;
    private static final double NEW_YORK_LON = -74.0060;

    private static final double LONDON_LAT = 51.5074;
    private static final double LONDON_LON = -0.1278;

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private WeatherApiService weatherApiService;

    @Mock
    private LocationDtoConverter locationDtoConverter;

    @Mock
    private WeatherResponseConverter weatherResponseConverter;

    @InjectMocks private LocationService locationService;

    @Test
    void getLocations_userHasLocations_returnsListOfLocations() {
        Location newYork = buildLocation(NEW_YORK_ID, NEW_YORK, NEW_YORK_LAT, NEW_YORK_LON);
        Location london = buildLocation(LONDON_ID, LONDON, LONDON_LAT, LONDON_LON);

        WeatherResponse nyWeather = new WeatherResponse();
        WeatherResponse londonWeather = new WeatherResponse();

        LocationDto nyDto = buildLocationDto(NEW_YORK_ID, NEW_YORK, NEW_YORK_LAT, NEW_YORK_LON);
        LocationDto londonDto = buildLocationDto(LONDON_ID, LONDON, LONDON_LAT, LONDON_LON);

        doReturn(List.of(newYork, london)).when(locationRepository).findByUserIdOrderByCreatedAt(USER_ID);
        mockWeatherFor(newYork, nyWeather);
        mockWeatherFor(london, londonWeather);
        doReturn(nyDto).when(locationDtoConverter).convert(nyWeather);
        doReturn(londonDto).when(locationDtoConverter).convert(londonWeather);

        var actual = locationService.getLocations(USER_ID);

        assertThat(actual).hasSize(2);
        assertThat(actual.getFirst().getName()).isEqualTo(NEW_YORK);
        assertThat(actual.getLast().getName()).isEqualTo(LONDON);

        verify(locationRepository).findByUserIdOrderByCreatedAt(USER_ID);
        verify(weatherApiService, times(2)).getWeatherByCoordinate(anyDouble(), anyDouble());
        verify(locationDtoConverter, times(2)).convert(any());
    }

    @Test
    void getLocations_userHasNotLocations_returnsEmptyList() {
        doReturn(List.of()).when(locationRepository).findByUserIdOrderByCreatedAt(USER_ID);

        var actual = locationService.getLocations(USER_ID);

        assertTrue(actual.isEmpty());

        verify(locationRepository).findByUserIdOrderByCreatedAt(USER_ID);
        verify(weatherApiService, never()).getWeatherByCoordinate(anyDouble(), anyDouble());
        verify(locationDtoConverter, never()).convert(any());
    }

    @Test
    void getLocations_converterReturnsNull_throwsException() {
        Location newYork = buildLocation(NEW_YORK_ID, NEW_YORK, NEW_YORK_LAT, NEW_YORK_LON);
        WeatherResponse weatherResponse = new WeatherResponse();

        doReturn(List.of(newYork)).when(locationRepository).findByUserIdOrderByCreatedAt(USER_ID);
        mockWeatherFor(newYork, weatherResponse);
        doReturn(null).when(locationDtoConverter).convert(weatherResponse);

        assertThrows(NullPointerException.class, () -> locationService.getLocations(USER_ID));

        verify(locationRepository).findByUserIdOrderByCreatedAt(USER_ID);
        verify(weatherApiService).getWeatherByCoordinate(anyDouble(), anyDouble());
        verify(locationDtoConverter).convert(weatherResponse);
    }

    @Test
    void getLocationsByCity_found_returnsList() {
        LocationResponse locationResponse = LocationResponse.builder()
                .name(NEW_YORK)
                .latitude(NEW_YORK_LAT)
                .longitude(NEW_YORK_LON)
                .build();

        doReturn(List.of(locationResponse)).when(weatherApiService).getLocations(NEW_YORK);

        var actual = locationService.getLocationsByCity(NEW_YORK);

        assertThat(actual).hasSize(1);
        assertThat(actual.getFirst().getName()).isEqualTo(NEW_YORK);

        verify(weatherApiService).getLocations(NEW_YORK);
    }

    @Test
    void getLocationsByCity_notFound_returnsEmptyList() {
        doReturn(List.of()).when(weatherApiService).getLocations(NEW_YORK);

        var actual = locationService.getLocationsByCity(NEW_YORK);

        assertTrue(actual.isEmpty());

        verify(weatherApiService).getLocations(NEW_YORK);
    }

    @Test
    void save_locationDoesNotExist_savesLocation() {
        User user = User.builder().id(USER_ID).build();
        WeatherResponse weatherResponse = new WeatherResponse();
        weatherResponse.setName(NEW_YORK);
        Location location = buildLocation(NEW_YORK_ID, NEW_YORK, NEW_YORK_LAT, NEW_YORK_LON);

        doReturn(weatherResponse).when(weatherApiService).getWeatherByCoordinate(NEW_YORK_LAT, NEW_YORK_LON);
        doReturn(false).when(locationRepository).existsByUserIdAndName(USER_ID, NEW_YORK);
        doReturn(location).when(weatherResponseConverter).convert(weatherResponse);
        doReturn(Optional.of(user)).when(userRepository).findById(USER_ID);
        doReturn(location).when(locationRepository).save(location);

        locationService.save(NEW_YORK_LAT, NEW_YORK_LON, USER_ID);

        assertEquals(user, location.getUser());

        verify(weatherApiService).getWeatherByCoordinate(NEW_YORK_LAT, NEW_YORK_LON);
        verify(locationRepository).existsByUserIdAndName(USER_ID, NEW_YORK);
        verify(weatherResponseConverter).convert(weatherResponse);
        verify(userRepository).findById(USER_ID);
        verify(locationRepository).save(location);
    }

    @Test
    void save_locationAlreadyExists_doesNotSave() {
        WeatherResponse weatherResponse = new WeatherResponse();
        weatherResponse.setName(NEW_YORK);

        doReturn(weatherResponse).when(weatherApiService).getWeatherByCoordinate(NEW_YORK_LAT, NEW_YORK_LON);
        doReturn(true).when(locationRepository).existsByUserIdAndName(USER_ID, NEW_YORK);

        locationService.save(NEW_YORK_LAT, NEW_YORK_LON, USER_ID);

        verify(weatherApiService).getWeatherByCoordinate(NEW_YORK_LAT, NEW_YORK_LON);
        verify(locationRepository).existsByUserIdAndName(USER_ID, NEW_YORK);
        verifyNoMoreInteractions(weatherResponseConverter, userRepository, locationRepository);
    }

    @Test
    void save_weatherApiThrows_throwsException() {
        doThrow(RuntimeException.class).when(weatherApiService).getWeatherByCoordinate(anyDouble(), anyDouble());

        assertThrows(RuntimeException.class,
                () -> locationService.save(anyDouble(), anyDouble(), USER_ID));

        verify(weatherApiService).getWeatherByCoordinate(anyDouble(), anyDouble());
        verifyNoMoreInteractions(locationRepository, weatherResponseConverter, userRepository);
    }

    @Test
    void deleteById_locationExists_deletes() {
        Location london = buildLocation(LONDON_ID, LONDON, LONDON_LAT, LONDON_LON);

        doReturn(Optional.of(london)).when(locationRepository).findById(LONDON_ID);

        locationService.deleteById(LONDON_ID);

        verify(locationRepository).findById(LONDON_ID);
        verify(locationRepository).delete(london);
        verify(locationRepository).flush();
    }

    @Test
    void deleteById_locationDoesNotExist_doesNothing() {
        doReturn(Optional.empty()).when(locationRepository).findById(LONDON_ID);

        locationService.deleteById(LONDON_ID);

        verify(locationRepository).findById(LONDON_ID);
        verify(locationRepository, never()).delete(any());
        verify(locationRepository, never()).flush();
    }

    private void mockWeatherFor(Location location, WeatherResponse response) {
        doReturn(response).when(weatherApiService)
                .getWeatherByCoordinate(location.getLatitude(), location.getLongitude());
    }

    private Location buildLocation(Long id, String name, double lat, double lon) {
        return Location.builder()
                .id(id)
                .name(name)
                .latitude(lat)
                .longitude(lon)
                .build();
    }

    private LocationDto buildLocationDto(Long id, String name, double lat, double lon) {
        var dto = new LocationDto();
        dto.setId(id);
        dto.setName(name);
        dto.setLatitude(lat);
        dto.setLongitude(lon);
        return dto;
    }
}