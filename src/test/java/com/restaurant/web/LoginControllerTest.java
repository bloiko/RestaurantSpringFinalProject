package com.restaurant.web;

import com.restaurant.database.entity.User;
import com.restaurant.service.UserService;
import com.restaurant.web.dto.LoginRequest;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpServerErrorException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {LoginController.class})
@EnableConfigurationProperties
class LoginControllerTest {

    private static final String USER_NAME = "userName";

    private static final String PASSWORD = "password";

    @Autowired
    private LoginController loginController;

    @MockBean
    private UserService userService;

    @Test
    void loginTest() {
        LoginRequest loginRequest = new LoginRequest(USER_NAME, PASSWORD, "firstName", "lastName");
        when(userService.login(USER_NAME, PASSWORD)).thenReturn(Optional.of("token"));
        String response = loginController.login(loginRequest);

        assertEquals("token", response);
    }

    @Test
    void loginTestReturnForbidden() {
        LoginRequest loginRequest = new LoginRequest(USER_NAME, PASSWORD, "firstName", "lastName");
        when(userService.login(USER_NAME, PASSWORD)).thenReturn(Optional.empty());
        HttpServerErrorException exception = assertThrows(HttpServerErrorException.class, () -> loginController.login(loginRequest));

        assertEquals(exception.getStatusCode(), HttpStatus.FORBIDDEN);
        assertEquals(exception.getMessage(), "403 Login Failed");
    }

    @Test
    void registrationTest() {
        LoginRequest loginRequest = new LoginRequest(USER_NAME, PASSWORD, "firstName", "lastName");
        User expectedUser = User.builder()
                .userName(USER_NAME)
                .password(PASSWORD)
                .firstName("firstName")
                .lastName("lastName")
                .build();
        when(userService.register(USER_NAME, PASSWORD, "firstName", "lastName")).thenReturn(Optional.of(expectedUser));

        User user = loginController.register(loginRequest);

        assertEquals(expectedUser, user);
    }

    @Test
    void registrationTestReturnBadRequest() {
        LoginRequest loginRequest = new LoginRequest(USER_NAME, PASSWORD, "firstName", "lastName");
        when(userService.register(USER_NAME, PASSWORD, "firstName", "lastName")).thenReturn(Optional.empty());

        HttpServerErrorException exception = assertThrows(HttpServerErrorException.class, () -> loginController.register(loginRequest));

        assertEquals(exception.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(exception.getMessage(), "400 User already exists");
    }
}