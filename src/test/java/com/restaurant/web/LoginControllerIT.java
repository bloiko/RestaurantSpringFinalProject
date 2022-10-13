package com.restaurant.web;

import com.restaurant.RestaurantApplication;
import com.restaurant.database.dao.UserRepository;
import com.restaurant.database.entity.Role;
import com.restaurant.database.entity.User;
import com.restaurant.security.jwt.JwtProvider;
import com.restaurant.web.dto.LoginRequest;
import com.restaurant.web.dto.RegistrationRequest;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

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

        String token = loginController.login(loginRequest);

        assertNotNull(token);
        assertEquals(USER_NAME, jwtProvider.getUsername(token));
//        TODO: uncomment this validation
//        List<GrantedAuthority> jwtProviderRoles = jwtProvider.getRoles(token);
//        assertEquals(1, jwtProviderRoles.size());
//        assertEquals("USER", jwtProviderRoles.get(0).getAuthority());
    }

    @Test
    public void testCorrectRegistration() {
        RegistrationRequest registerRequest = new RegistrationRequest(USER_NAME, PASSWORD, "firstName", "lastName", "email@mail.com");

        User user = loginController.register(registerRequest);

        assertNotNull(user);
        assertEquals(user, userRepository.getById(user.getId()));
    }

    @NotNull
    private User buildUser() {
        return new User(USER_NAME, passwordEncoder.encode(PASSWORD), "firstName", "lastName",
                "email@mail.com", new Role(1L, "USER"));
    }
}
