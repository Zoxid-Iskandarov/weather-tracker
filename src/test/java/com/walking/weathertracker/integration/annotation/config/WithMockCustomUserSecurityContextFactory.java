package com.walking.weathertracker.integration.annotation.config;

import com.walking.weathertracker.config.security.CustomUserDetails;
import com.walking.weathertracker.integration.annotation.WithMockCustomUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Collections;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {
    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        CustomUserDetails userDetails = new CustomUserDetails(
                annotation.id(),
                annotation.username(),
                "$2a$10$7Xz5x9m7Y0Xz5x9m7Y0Xz5x9m7Y0Xz5x9m7Y0Xz5x9m7Y0Xz5x9m",
                Collections.emptyList());

        context.setAuthentication(new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()));

        return context;
    }
}
