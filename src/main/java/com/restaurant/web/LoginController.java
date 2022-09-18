package com.restaurant.web;

import com.restaurant.database.entity.User;
import com.restaurant.service.UserService;
import com.restaurant.web.dto.LoginRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;

@RestController
public class LoginController {
    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/signin")
    public String login(@RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest.getUsername(), loginRequest.getPassword()).orElseThrow(() ->
                new HttpServerErrorException(HttpStatus.FORBIDDEN, "Login Failed"));
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public User register(@RequestBody LoginRequest registrationDto) {
        return userService.register(registrationDto.getUsername(), registrationDto.getPassword(), registrationDto.getFirstName(),
                registrationDto.getLastName()).orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "User already exists"));
    }
}
