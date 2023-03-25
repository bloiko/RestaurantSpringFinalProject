package com.restaurant.web;

import com.restaurant.service.UserService;
import com.restaurant.web.dto.LoginRequest;
import com.restaurant.web.dto.LoginResponse;
import com.restaurant.web.dto.RegistrationRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.util.Assert.hasText;

@RestController
@RequestMapping("/security")
public class LoginController {
    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/signin")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        hasText(loginRequest.getUsername(), "Username must be specified");
        hasText(loginRequest.getPassword(), "Password must be specified");
        return LoginResponse.of(userService.login(loginRequest.getUsername(), loginRequest.getPassword()));
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public LoginResponse register(@Valid @RequestBody RegistrationRequest registrationDto) {
        return LoginResponse.of(userService.register(registrationDto));
    }
}
