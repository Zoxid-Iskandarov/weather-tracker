package com.walking.weathertracker.converter.user;

import com.walking.weathertracker.domain.User;
import com.walking.weathertracker.model.user.CreateUserRequest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CreateUserRequestConverter implements Converter<CreateUserRequest, User> {
    @Override
    public User convert(CreateUserRequest source) {
        User user = new User();

        user.setUsername(source.getUsername());
        user.setPassword(source.getPassword());

        return user;
    }
}
