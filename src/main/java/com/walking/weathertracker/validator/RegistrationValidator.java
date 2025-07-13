package com.walking.weathertracker.validator;

import com.walking.weathertracker.model.user.CreateUserRequest;
import com.walking.weathertracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class RegistrationValidator implements Validator {
    private final UserRepository userRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return CreateUserRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CreateUserRequest request = (CreateUserRequest) target;

        if (userRepository.existsByUsername(request.getUsername())) {
            errors.rejectValue("username", "409", "Username already exists");
        }

        if (!request.getPassword().equals(request.getRepeatPassword())) {
            errors.rejectValue("repeatPassword", "400", "Passwords do not match");
        }
    }
}
