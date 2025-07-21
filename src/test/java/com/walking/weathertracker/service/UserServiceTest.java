package com.walking.weathertracker.service;

import com.walking.weathertracker.config.security.CustomUserDetails;
import com.walking.weathertracker.converter.user.CreateUserRequestConverter;
import com.walking.weathertracker.domain.User;
import com.walking.weathertracker.model.user.CreateUserRequest;
import com.walking.weathertracker.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private CreateUserRequestConverter createUserRequestConverter;

    @InjectMocks
    private UserService userService;

    @Test
    void create_success() {
        CreateUserRequest userRequest = getUserRequest();

        User user = getUserAfterConversion();

        String encodedPassword = "passwordTest";

        doReturn(user).when(createUserRequestConverter).convert(userRequest);
        doReturn(encodedPassword).when(passwordEncoder).encode(userRequest.getPassword());
        doReturn(user).when(userRepository).save(user);

        userService.create(userRequest);
        assertEquals(encodedPassword, user.getPassword());

        verify(createUserRequestConverter).convert(userRequest);
        verify(passwordEncoder).encode(encodedPassword);
        verify(userRepository).save(user);
    }

    @Test
    void create_whenConverterReturnNull_failed() {
        CreateUserRequest userRequest = getUserRequest();

        doReturn(null).when(createUserRequestConverter).convert(userRequest);

        userService.create(userRequest);

        verify(createUserRequestConverter).convert(userRequest);
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void create_whenRepositoryThrowException_failed() {
        CreateUserRequest userRequest = getUserRequest();
        User user = getUserAfterConversion();

        doReturn(user).when(createUserRequestConverter).convert(userRequest);
        doReturn("encodedPassword").when(passwordEncoder).encode(anyString());
        doThrow(DataIntegrityViolationException.class).when(userRepository).save(user);

        assertThrows(DataIntegrityViolationException.class, () -> userService.create(userRequest));
        assertEquals("encodedPassword", user.getPassword());

        verify(createUserRequestConverter).convert(userRequest);
        verify(passwordEncoder).encode(anyString());
        verify(userRepository).save(user);
    }

    @Test
    void loadUserByUsername_success() {
        User user = getUser();

        doReturn(Optional.of(user)).when(userRepository).findByUsername(user.getUsername());

        var actual = (CustomUserDetails) userService.loadUserByUsername(user.getUsername());

        assertEquals(user.getId(), actual.getId());
        assertEquals(user.getUsername(), actual.getUsername());
        assertEquals(user.getPassword(), actual.getPassword());
        assertTrue(actual.getAuthorities().isEmpty());

        verify(userRepository).findByUsername(user.getUsername());
    }

    @Test
    void loadUserByUsername_whenUserNotFound_failed() {
        doReturn(Optional.empty()).when(userRepository).findByUsername(anyString());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(anyString()));

        verify(userRepository).findByUsername(anyString());
    }

    private CreateUserRequest getUserRequest() {
        return CreateUserRequest.builder()
                .username("usernameTest")
                .password("passwordTest")
                .repeatPassword("passwordTest")
                .build();
    }

    private User getUserAfterConversion() {
        return User.builder()
                .username("usernameTest")
                .password("passwordTest")
                .build();
    }

    private User getUser() {
        return User.builder()
                .id(1L)
                .username("usernameTest")
                .password("passwordTest")
                .locations(List.of())
                .build();
    }
}