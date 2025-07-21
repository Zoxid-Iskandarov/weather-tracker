package com.walking.weathertracker.model.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateUserRequest {
    @NotBlank(message = "Username cannot be empty.")
    @Email(message = "Username must be a valid email.")
    private String username;

    @NotBlank(message = "Password cannot be empty.")
    @Size(min = 6, message = "Password must be at least 6 characters long.")
    private String password;

    @NotBlank(message = "This field cannot be empty.")
    private String repeatPassword;
}
