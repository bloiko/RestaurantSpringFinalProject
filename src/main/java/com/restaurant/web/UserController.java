package com.restaurant.web;

import com.restaurant.database.entity.User;
import com.restaurant.service.UserService;
import com.restaurant.web.dto.PasswordDto;
import com.restaurant.web.dto.UserDto;
import com.restaurant.web.mapper.UserDtoMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    private final UserDtoMapper userDtoMapper;

    public UserController(UserService userService, UserDtoMapper userDtoMapper) {
        this.userService = userService;
        this.userDtoMapper = userDtoMapper;
    }

    @GetMapping("/{userId}")
    public UserDto getUserDetailsById(@PathVariable Long userId) {
        User user = userService.getUserDetailsById(userId);

        return userDtoMapper.mapUserToDto(user);
    }

    @PutMapping("/{userId}")
    public UserDto updateUserDetails(@PathVariable Long userId, @Valid @RequestBody UserDto userDto) {
        User user = userService.updateUserDetails(userId, userDto);

        return userDtoMapper.mapUserToDto(user);
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<Boolean> updatePassword(@PathVariable Long userId, @Valid @RequestBody PasswordDto passwordDto) {
        userService.updatePassword(userId, passwordDto);

        return ResponseEntity.ok(Boolean.TRUE);
    }
}
