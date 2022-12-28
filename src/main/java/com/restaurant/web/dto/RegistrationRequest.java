package com.restaurant.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequest {
    @NotEmpty(message = "First name must be filled!")
    private String firstName;

    @NotEmpty(message = "Last name must be filled!")
    private String lastName;

    @NotEmpty(message = "User name must be filled!")
    private String username;

    @NotEmpty(message = "Password must be filled!")
    private String password;

    @NotEmpty(message = "Email must be filled!")
    @Email(message = "Incorrect email format!")
    private String email;
}
