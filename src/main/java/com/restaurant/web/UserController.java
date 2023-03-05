package com.restaurant.web;

import com.restaurant.database.entity.User;
import com.restaurant.service.UserService;
import com.restaurant.web.dto.PasswordDto;
import com.restaurant.web.dto.UserDto;
import com.restaurant.web.mapper.UserDtoMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

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

    @GetMapping("/profile")
    public UserDto getUserDetailsById() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userService.getUserByUserName(userDetails.getUsername());

        if (user == null) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "User not found");
        }
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

    @GetMapping ("/all")
    public List<UserDto> getAllUsersDetails() {
        List<User> users = userService.getAllUsers();

        return users.stream().map(userDtoMapper::mapUserToDto).collect(Collectors.toList());
    }

    @DeleteMapping ("/{userId}")
    public String deleteUserById(@PathVariable Long userId) {
        return userService.deleteUserById(userId);
    }
}
