package com.restaurant.service;


import com.restaurant.database.dao.OrderRepository;
import com.restaurant.database.dao.RoleRepository;
import com.restaurant.database.dao.UserRepository;
import com.restaurant.database.entity.Order;
import com.restaurant.database.entity.OrderStatus;
import com.restaurant.database.entity.Role;
import com.restaurant.database.entity.User;
import com.restaurant.security.jwt.JwtProvider;
import com.restaurant.messaging.email.EmailMessagesSender;
import com.restaurant.messaging.email.EmailService;
import com.restaurant.web.dto.RegistrationRequest;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpServerErrorException;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {UserService.class})
@EnableConfigurationProperties
class UserServiceTest {
    private static final String USER_NAME = "userName";

    private static final String PASSWORD = "password";

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private EmailService emailService;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private JwtProvider jwtProvider;

    @MockBean
    private EmailMessagesSender emailMessagesSender;

    @Test
    void isCorrectAdmin() {
        when(userRepository.findByUserName(USER_NAME)).thenReturn(Optional.of(buildAdminUser()));

        boolean result = userService.isCorrectAdmin(USER_NAME, PASSWORD);

        assertTrue(result);
    }

    @Test
    void getUserOrdersSortByOrderDateReversed() {
        User simpleUser = buildSimpleUser();
        when(userRepository.findByUserName(USER_NAME)).thenReturn(Optional.of(simpleUser));
        List<Order> notSortedOrders = buildOrdersListNotSorted();
        when(orderRepository.findAllByUserId(simpleUser.getId())).thenReturn(notSortedOrders);

        List<Order> orders = userService.getUserOrdersSortByOrderDateReversed(USER_NAME);

        List<Order> sortedOrders = notSortedOrders.stream().sorted(Comparator.comparing(Order::getOrderDate).reversed()).collect(Collectors.toList());
        assertEquals(sortedOrders, orders);
    }

    @Test
    void testAddUserAndReturnIdThatUserExists() {
        when(userRepository.findByUserName(USER_NAME)).thenReturn(Optional.of(buildSimpleUser()));

        Long resultId = userService.addUserAndReturnId(buildSimpleUser());

        assertEquals(-1L, resultId);
    }

    @Test
    void testAddUserAndReturnIdUserNotExistsInDb() {
        when(userRepository.findByUserName(USER_NAME)).thenReturn(Optional.empty());
        User user = buildSimpleUser();
        when(userRepository.save(user)).thenReturn(user);

        Long resultId = userService.addUserAndReturnId(user);

        assertEquals(user.getId(), resultId);
    }

    @Test
    void isCorrectUserWithRoleUser() {
        when(userRepository.findByUserName(USER_NAME)).thenReturn(Optional.of(buildSimpleUser()));
        when(passwordEncoder.matches(PASSWORD, PASSWORD)).thenReturn(true);

        boolean result = userService.isCorrectUser(USER_NAME, PASSWORD);

        assertTrue(result);
    }

    @Test
    void isCorrectUserWithRoleAdmin() {
        when(userRepository.findByUserName(USER_NAME)).thenReturn(Optional.of(buildAdminUser()));
        when(passwordEncoder.matches(PASSWORD, PASSWORD)).thenReturn(true);

        boolean result = userService.isCorrectUser(USER_NAME, PASSWORD);

        assertTrue(result);
    }

    @Test
    void isCorrectUserNullTest() {
        when(userRepository.findByUserName(USER_NAME)).thenReturn(Optional.empty());

        boolean result = userService.isCorrectUser(USER_NAME, PASSWORD);

        assertFalse(result);
    }

    @Test
    void getUserByUserName() {
        User user = buildSimpleUser();
        when(userRepository.findByUserName(USER_NAME)).thenReturn(Optional.of(user));

        User result = userService.getUserByUserName(USER_NAME);

        assertEquals(user, result);
    }

    @Test
    void getUserByUserNameThatUserNull() {
        when(userRepository.findByUserName(USER_NAME)).thenReturn(Optional.empty());

        User result = userService.getUserByUserName(USER_NAME);

        assertNull(result);
    }


    @Test
    void loginTestReturnUnknownUser() {
        when(userRepository.findByUserName(USER_NAME)).thenReturn(Optional.empty());

        HttpServerErrorException exception = assertThrows(HttpServerErrorException.class, () -> userService.login(USER_NAME, PASSWORD));

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        assertEquals("403 Unknown username", exception.getMessage());
    }

    @Test
    void loginTestReturnIncorrectPassword() {
        when(userRepository.findByUserName(USER_NAME)).thenReturn(Optional.of(buildSimpleUser()));
        String incorrectPassword = "incorrectPassword";
        when(passwordEncoder.matches(PASSWORD, incorrectPassword)).thenReturn(false);

        HttpServerErrorException exception = assertThrows(HttpServerErrorException.class, () -> userService.login(USER_NAME, incorrectPassword));

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        assertEquals("403 Password is not correct", exception.getMessage());
    }

    @Test
    void registrationTestReturnBadRequest() {
        when(userRepository.findByUserName(USER_NAME)).thenReturn(Optional.of(buildSimpleUser()));

        RegistrationRequest registrationRequest = new RegistrationRequest(null, PASSWORD, USER_NAME, null, null);
        HttpServerErrorException exception = assertThrows(HttpServerErrorException.class, () -> userService.register(registrationRequest));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("400 User already exists", exception.getMessage());
    }

    private static User buildAdminUser() {
        return buildUserWithRole(new Role(2L, "ADMIN"));
    }

    private static User buildSimpleUser() {
        return buildUserWithRole(new Role(1L, "USER"));
    }

    private static User buildUserWithRole(Role role) {
        return new User(1L, "firstName", "lastName",
                "userName", "password", "email@mail.com",
                "address", "+388888888888", role);
    }

    private List<Order> buildOrdersListNotSorted() {
        Order firstOrder = new Order(1L, new Timestamp(100000000000000L),
                new BigDecimal(10), buildSimpleUser(), new ArrayList<>(), new OrderStatus());
        Order secondOrder = new Order(2L, new Timestamp(10000L),
                new BigDecimal(10), buildSimpleUser(), new ArrayList<>(), new OrderStatus());
        Order thirdOrder = new Order(3L, new Timestamp(5555555555L),
                new BigDecimal(10), buildSimpleUser(), new ArrayList<>(), new OrderStatus());
        return Arrays.asList(firstOrder, secondOrder, thirdOrder);
    }
}