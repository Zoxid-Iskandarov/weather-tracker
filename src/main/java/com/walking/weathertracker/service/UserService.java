package com.walking.weathertracker.service;

import com.walking.weathertracker.converter.user.CreateUserRequestConverter;
import com.walking.weathertracker.config.security.CustomUserDetails;
import com.walking.weathertracker.model.user.CreateUserRequest;
import com.walking.weathertracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CreateUserRequestConverter createUserRequestConverter;

    @Transactional
    public void create(CreateUserRequest userRequest) {
        Optional.ofNullable(createUserRequestConverter.convert(userRequest))
                .ifPresent(user -> {
                    user.setPassword(passwordEncoder.encode(user.getPassword()));
                    userRepository.save(user);
                });
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(user -> new CustomUserDetails(
                        user.getId(),
                        user.getUsername(),
                        user.getPassword(),
                        Collections.emptyList()
                )).orElseThrow(() -> new UsernameNotFoundException(
                        "User with username %s not found".formatted(username)));
    }
}
