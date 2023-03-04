package com.restaurant.web;

import com.restaurant.database.entity.Role;
import com.restaurant.database.entity.User;
import com.restaurant.service.UserService;
import com.restaurant.web.dto.LoginRequest;
import com.restaurant.web.dto.LoginResponse;
import com.restaurant.web.dto.RegistrationRequest;
import com.restaurant.web.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {LoginController.class})
@EnableConfigurationProperties
class LoginControllerTest {

    private static final String USER_NAME = "userName";

    private static final String PASSWORD = "password";

    public static final String EMAIL = "email@email.com";

    @Autowired
    private LoginController loginController;

    @MockBean
    private UserService userService;

    @Test
    void loginTest() {
        LoginRequest loginRequest = new LoginRequest(USER_NAME, PASSWORD);
        when(userService.login(USER_NAME, PASSWORD)).thenReturn("token");

        LoginResponse response = loginController.login(loginRequest);

        assertNotNull(response);
        assertEquals("token", response.getAuthToken());
    }

    @Test
    void registrationTest() {
        RegistrationRequest registrationRequest = new RegistrationRequest(USER_NAME, PASSWORD, "firstName", "lastName", EMAIL);
        User expectedUser = User.builder()
                .userName(USER_NAME)
                .password(PASSWORD)
                .firstName("firstName")
                .lastName("lastName")
                .email(EMAIL)
                .role(new Role(1L, "USER"))
                .build();
        when(userService.register(registrationRequest)).thenReturn("token");

        LoginResponse loginResponse = loginController.register(registrationRequest);

        assertNotNull(loginResponse);
        assertEquals("token", loginResponse.getAuthToken());
    }
}