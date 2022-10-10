package com.restaurant.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RegistrationRequest {
    private String username;

    private String password;

    private String firstName;

    private String lastName;

    private String email;
}
