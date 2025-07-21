package com.walking.weathertracker.integration.service;

import com.walking.weathertracker.config.security.CustomUserDetails;
import com.walking.weathertracker.integration.IntegrationTestBase;
import com.walking.weathertracker.model.user.CreateUserRequest;
import com.walking.weathertracker.repository.UserRepository;
import com.walking.weathertracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
public class UserServiceIT extends IntegrationTestBase {
    private static final Long USER_ID = 1L;
    private static final String EXISTS_USERNAME = "johndoe@gmail.com";
    private static final String NOT_EXISTS_USERNAME = "tomShelbi12@gmail.com";

    private final UserService userService;
    private final UserRepository userRepository;

    @Test
    void loadUserByUsername_success() {
        var userDetails = (CustomUserDetails) userService.loadUserByUsername(EXISTS_USERNAME);

        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getId()).isEqualTo(USER_ID);
        assertEquals(EXISTS_USERNAME, userDetails.getUsername());
    }

    @Test
    void loadUserByUsername_whenUserNotFound_failed() {
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(NOT_EXISTS_USERNAME));
    }

    @Test
    void create_success() {
        var userRequest = getUserRequest();
        userService.create(userRequest);

        var actual = userRepository.findByUsername(userRequest.getUsername());
        assertThat(actual).isPresent();
        assertThat(actual.get().getId()).isNotNull();
        assertThat(actual.get().getUsername()).isEqualTo(userRequest.getUsername());
        assertThat(actual.get().getPassword()).isNotEqualTo(userRequest.getPassword());
        assertThat(actual.get().getCreatedAt()).isNotNull();
    }

    @Test
    void create_whenUserAlreadyExists_failed() {
        var userRequest = getUserRequest();
        userRequest.setUsername(EXISTS_USERNAME);

        assertThrows(DataIntegrityViolationException.class, () -> userService.create(userRequest));
    }

    private CreateUserRequest getUserRequest() {
        return CreateUserRequest.builder()
                .username("usernameTest")
                .password("passwordTest")
                .repeatPassword("passwordTest")
                .build();
    }
}