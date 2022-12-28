package com.restaurant.web;

import com.restaurant.database.entity.User;
import com.restaurant.service.UserService;
import com.restaurant.web.dto.LoginRequest;
import com.restaurant.web.dto.RegistrationRequest;
import com.restaurant.web.dto.UserDto;
import com.restaurant.web.mapper.UserDtoMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.util.Assert.hasText;

@RestController
@RequestMapping("/security")
public class LoginController {
    private final UserService userService;

    private final UserDtoMapper userDtoMapper = new UserDtoMapper();

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
    public UserDto register(@Valid @RequestBody RegistrationRequest registrationDto) {
        User user = userService.register(registrationDto);
        return userDtoMapper.mapUserToDto(user);
    }
}
