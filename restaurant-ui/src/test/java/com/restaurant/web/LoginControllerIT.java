package com.restaurant.web;

import com.restaurant.RestaurantApplication;
import com.restaurant.database.dao.UserRepository;
import com.restaurant.database.entity.Role;
import com.restaurant.database.entity.RoleName;
import com.restaurant.database.entity.User;
import com.restaurant.security.jwt.JwtProvider;
import com.restaurant.messaging.email.EmailMessagesSender;
import com.restaurant.web.dto.LoginRequest;
import com.restaurant.web.dto.LoginResponse;
import com.restaurant.web.dto.RegistrationRequest;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest(classes = RestaurantApplication.class)
@TestPropertySource(locations="classpath:application-test.properties")
@Sql({"classpath:db-test/test-user-data.sql"})
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

    @MockBean
    private EmailMessagesSender emailMessagesSender;

    @Test
    public void testCorrectLogin() {
        LoginRequest loginRequest = new LoginRequest(USER_NAME, PASSWORD);
        userRepository.save(buildUser());

        LoginResponse response = loginController.login(loginRequest);

        assertNotNull(response);
        assertNotNull(response.getAuthToken());
        assertEquals(USER_NAME, jwtProvider.getUsername(response.getAuthToken()));
        List<GrantedAuthority> jwtProviderRoles = jwtProvider.getRoles(response.getAuthToken());
        assertEquals(1, jwtProviderRoles.size());
        assertEquals("USER", jwtProviderRoles.get(0).getAuthority());
    }

    @Test
    public void testCorrectRegistration() {
        RegistrationRequest registerRequest = new RegistrationRequest("firstName", "lastName", USER_NAME, PASSWORD, "email@mail.com");

        LoginResponse response = loginController.register(registerRequest);

        assertNotNull(response);
        assertNotNull(response.getAuthToken());
        assertEquals(USER_NAME, jwtProvider.getUsername(response.getAuthToken()));
        List<GrantedAuthority> jwtProviderRoles = jwtProvider.getRoles(response.getAuthToken());
        assertEquals(1, jwtProviderRoles.size());
        assertEquals("USER", jwtProviderRoles.get(0).getAuthority());
    }

    @NotNull
    private User buildUser() {
        return new User(USER_NAME, passwordEncoder.encode(PASSWORD), "firstName", "lastName",
                "email@mail.com", new Role(1L, RoleName.USER));
    }
}
