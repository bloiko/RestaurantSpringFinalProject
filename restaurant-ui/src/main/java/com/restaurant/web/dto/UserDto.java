package com.restaurant.web.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private Long id;

    @NotEmpty(message = "First name must be filled!")
    private String firstName;

    @NotEmpty(message = "Last name must be filled!")
    private String lastName;

    @NotEmpty(message = "User name must be filled!")
    private String username;

    private String password;

    @NotEmpty(message = "Email must be filled!")
    @Email(message = "Incorrect email format!")
    private String email;

    private String address;

    private String phoneNumber;

    private String role;

}
