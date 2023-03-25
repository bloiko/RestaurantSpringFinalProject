package com.restaurant.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

    private String authToken;

    public static LoginResponse of(String authToken) {
        return new LoginResponse(authToken);
    }
}
