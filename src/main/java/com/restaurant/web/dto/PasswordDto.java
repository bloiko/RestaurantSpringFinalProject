package com.restaurant.web.dto;

import com.restaurant.web.util.password.ValidPassword;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordDto {

    @NotEmpty(message = "Old password must be filled!")
    private String oldPassword;

    @NotEmpty(message = "New password must be filled!")
    @ValidPassword
    private String newPassword;
}
