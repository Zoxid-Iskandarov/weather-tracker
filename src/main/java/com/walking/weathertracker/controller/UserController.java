package com.walking.weathertracker.controller;

import com.walking.weathertracker.model.user.CreateUserRequest;
import com.walking.weathertracker.service.UserService;
import com.walking.weathertracker.validator.RegistrationValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final RegistrationValidator registrationValidator;

    @GetMapping("/sign-in")
    public String getLoginPage() {
        return "sign-in";
    }

    @GetMapping("/sign-up")
    public String registrationPage(@ModelAttribute("user") CreateUserRequest user) {
        return "sign-up";
    }

    @PostMapping("/sign-up")
    public String createUser(@Validated @ModelAttribute("user") CreateUserRequest userRequest,
                             BindingResult bindingResult) {
        registrationValidator.validate(userRequest, bindingResult);

        if (bindingResult.hasErrors()) {
            return "sign-up";
        }

        userService.create(userRequest);
        return "redirect:/sign-in";
    }
}
