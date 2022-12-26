package com.restaurant.web;

import com.restaurant.RestaurantApplication;
import com.restaurant.database.dao.UserRepository;
import com.restaurant.database.entity.Role;
import com.restaurant.database.entity.User;
import com.restaurant.security.jwt.JwtProvider;
import com.restaurant.web.dto.LoginRequest;
import com.restaurant.web.dto.LoginResponse;
import com.restaurant.web.dto.RegistrationRequest;
import com.restaurant.web.dto.UserDto;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest(classes = RestaurantApplication.class)
public class LoginControllerIT {

    public static final String USER_NAME = "username";

    public static final String PASSWORD = "password";
    @Autowired
    private LoginController loginController;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtProvider jwtProvider;


    @Test
    public void testCorrectLogin() {
        LoginRequest loginRequest = new LoginRequest(USER_NAME, PASSWORD);
        userRepository.save(buildUser());

        LoginResponse response = loginController.login(loginRequest);

        assertNotNull(response);
        assertEquals(USER_NAME, jwtProvider.getUsername(response.getToken()));
        List<GrantedAuthority> jwtProviderRoles = jwtProvider.getRoles(response.getToken());
        assertEquals(1, jwtProviderRoles.size());
        assertEquals("USER", jwtProviderRoles.get(0).getAuthority());
    }

    @Test
    public void testCorrectRegistration() {
        RegistrationRequest registerRequest = new RegistrationRequest(USER_NAME, PASSWORD, "firstName", "lastName", "email@mail.com");

        UserDto user = loginController.register(registerRequest);

        assertNotNull(user);
        assertNotNull(user.getId());
        assertEquals(registerRequest.getUsername(), user.getUserName());
        assertEquals(registerRequest.getEmail(), user.getEmail());
        assertEquals(registerRequest.getFirstName(), user.getFirstName());
        assertEquals(registerRequest.getLastName(), user.getLastName());
        assertEquals("USER", user.getRole());
        assertEquals(null, user.getPassword());
    }

    @NotNull
    private User buildUser() {
        return new User(USER_NAME, passwordEncoder.encode(PASSWORD), "firstName", "lastName",
                "email@mail.com", new Role(1L, "USER"));
    }
}
