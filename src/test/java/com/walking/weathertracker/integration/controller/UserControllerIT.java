package com.walking.weathertracker.integration.controller;

import com.walking.weathertracker.integration.IntegrationTestBase;
import com.walking.weathertracker.model.user.CreateUserRequest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@RequiredArgsConstructor
public class UserControllerIT extends IntegrationTestBase {
    private final MockMvc mockMvc;

    @Test
    void getLoginPage_returnView() throws Exception {
        mockMvc.perform(get("/sign-in"))
                .andExpect(status().isOk())
                .andExpect(view().name("sign-in"));
    }

    @Test
    void getRegistrationPage_returnView() throws Exception {
        mockMvc.perform(get("/sign-up"))
                .andExpect(status().isOk())
                .andExpect(view().name("sign-up"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    void createUser_whenValidRequest_redirectToSignIn() throws Exception {
        var userRequest = getCreateUserRequest();

        mockMvc.perform(post("/sign-up")
                        .flashAttr("user", userRequest)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/sign-in"));
    }

    @Test
    void createUser_whenInvalidRequest_returnViewSignUp() throws Exception {
        var userRequest = getCreateUserRequest();
        userRequest.setRepeatPassword("differentPassword");

        mockMvc.perform(post("/sign-up")
                .flashAttr("user", userRequest)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("sign-up"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("user", "repeatPassword"));
    }

    private CreateUserRequest getCreateUserRequest() {
        return CreateUserRequest.builder()
                .username("newuser@gmail.com")
                .password("testPassword")
                .repeatPassword("testPassword")
                .build();
    }
}
