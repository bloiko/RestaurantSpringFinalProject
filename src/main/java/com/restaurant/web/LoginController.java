package com.restaurant.web;

import com.restaurant.database.entity.User;
import com.restaurant.service.UserService;
import com.restaurant.web.dto.LoginRequest;
import com.restaurant.web.dto.RegistrationRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static io.jsonwebtoken.lang.Assert.hasText;

@RestController
public class LoginController {
    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/signin")
    public String login(@RequestBody LoginRequest loginRequest) {
        hasText(loginRequest.getUsername(), "Username must be specified");
        hasText(loginRequest.getPassword(), "Password must be specified");
        return userService.login(loginRequest.getUsername(), loginRequest.getPassword());
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public User register(@RequestBody RegistrationRequest registrationDto) {
        return userService.register(registrationDto);
    }
}
