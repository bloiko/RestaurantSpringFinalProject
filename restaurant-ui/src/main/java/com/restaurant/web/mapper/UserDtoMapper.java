package com.restaurant.web.mapper;

import com.restaurant.database.entity.User;
import com.restaurant.web.dto.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserDtoMapper {

    public UserDtoMapper() {
    }

    public UserDto mapUserToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUserName())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .role(user.getRole().getRoleName().name())
                .build();
    }
}