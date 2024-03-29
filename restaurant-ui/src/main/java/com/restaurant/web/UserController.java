package com.restaurant.web;

import com.restaurant.database.entity.User;
import com.restaurant.service.UserService;
import com.restaurant.web.dto.GetUsersResponse;
import com.restaurant.web.dto.PasswordDto;
import com.restaurant.web.dto.UserDto;
import com.restaurant.web.dto.UsersPage;
import com.restaurant.web.mapper.UserDtoMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.ObjectUtils.isEmpty;

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
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
        User user = userService.getById(userId);

        return userDtoMapper.mapUserToDto(user);
    }

    @GetMapping("/profile")
    public UserDto getCurrentUserDetails() {
        Authentication userDetails = SecurityContextHolder.getContext().getAuthentication();

        User user = userService.getUserByUserName(userDetails.getName());

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
        List<User> users = userService.getAll();

        return users.stream().map(userDtoMapper::mapUserToDto).collect(Collectors.toList());
    }

    @GetMapping("/pageable")
    public GetUsersResponse getAllUsersDetailsByPage(@RequestBody UsersPage usersPage) {
        Page<User> users = userService.getAllUsersByPage(usersPage);

        List<UserDto> userDtos = users.getContent()
                .stream()
                .map(userDtoMapper::mapUserToDto)
                .collect(Collectors.toList());

        return GetUsersResponse.builder()
                .page(usersPage.getPageNumber())
                .numOfPages(users.getTotalPages())
                .users(userDtos)
                .build();
    }

    @DeleteMapping ("/{userId}")
    public String deleteUserById(@PathVariable Long userId) {
        if (isEmpty(userId)) {
            throw new IllegalArgumentException("User id is incorrect");
        }

        return userService.deleteUserById(userId);
    }
}
