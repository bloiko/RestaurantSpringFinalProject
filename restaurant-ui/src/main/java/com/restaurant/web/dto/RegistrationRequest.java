package com.restaurant.web.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.restaurant.web.util.password.ValidPassword;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
@NoArgsConstructor
@ToString(exclude = "password")
public class RegistrationRequest implements Serializable {
    @NotEmpty(message = "First name must be filled!")
    private String firstName;

    @NotEmpty(message = "Last name must be filled!")
    private String lastName;

    @NotEmpty(message = "User name must be filled!")
    private String username;

    @ValidPassword
    private String password;

    @NotEmpty(message = "Email must be filled!")
    @Email(message = "Incorrect email format!")
    private String email;

    @JsonCreator
    public RegistrationRequest(@JsonProperty("firstName") String firstName,
                               @JsonProperty("lastName") String lastName,
                               @JsonProperty("username") String username,
                               @JsonProperty("password") String password,
                               @JsonProperty("email") String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.email = email;
    }
}
