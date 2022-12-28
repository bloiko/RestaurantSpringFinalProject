package com.restaurant.service;


import com.restaurant.database.dao.OrderRepository;
import com.restaurant.database.dao.RoleRepository;
import com.restaurant.database.dao.UserRepository;
import com.restaurant.database.entity.Order;
import com.restaurant.database.entity.Role;
import com.restaurant.database.entity.User;
import com.restaurant.security.jwt.JwtProvider;
import com.restaurant.web.dto.RegistrationRequest;
import com.restaurant.web.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.springframework.util.ObjectUtils.isEmpty;

/**
 * User service.
 *
 * @author B.Loiko
 */

@Slf4j
@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;

    private final OrderRepository orderRepository;

    private final AuthenticationManager authenticationManager;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtProvider jwtProvider;

    private final EmailService emailService;

    @Autowired
    public UserService(UserRepository userRepository, OrderRepository orderRepository, AuthenticationManager authenticationManager,
                       RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider, EmailService emailService) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.authenticationManager = authenticationManager;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.emailService = emailService;
    }

    public String login(String username, String password) {
        log.info("New user attempting to sign in");
        User user = validateUserExistence(username);

        validatePassword(password, user);

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            return jwtProvider.createToken(username, user.getRole());
        } catch (AuthenticationException e) {
            log.info("Log in failed for user {}", username);
            throw new RuntimeException(e);
        }
    }

    public User register(RegistrationRequest registrationRequest) {
        User registeredUserInDb = registerUserInDb(registrationRequest);

        try {
            emailService.sendSuccessfulRegistrationEmail(registrationRequest);
        } catch (MessagingException | IOException e) {
            log.error("Cannot send email to user with email = {}", registeredUserInDb.getEmail());
        }

        return registeredUserInDb;
    }

    private User registerUserInDb(RegistrationRequest registrationRequest) {
        log.info("New user attempting to sign in");
        Optional<User> user = userRepository.findByUserName(registrationRequest.getUsername());

        if (user.isPresent()) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "User already exists");
        }

        return createUser(registrationRequest).orElse(null);
    }

    @NotNull
    private Optional<User> createUser(RegistrationRequest registrationRequest) {
        Optional<Role> role = roleRepository.findByName("USER");
        return Optional.of(userRepository.save(buildUserFromRequest(registrationRequest, role)));
    }

    @NotNull
    private User buildUserFromRequest(RegistrationRequest registrationRequest, Optional<Role> role) {
        return new User(registrationRequest.getUsername(),
                passwordEncoder.encode(registrationRequest.getPassword()),
                registrationRequest.getFirstName(),
                registrationRequest.getLastName(),
                registrationRequest.getEmail(),
                role.get());
    }

    @NotNull
    private User validateUserExistence(String username) {
        User user = getUserByUserName(username);
        if (user == null) {
            throw new HttpServerErrorException(HttpStatus.FORBIDDEN, "Unknown username");
        }
        return user;
    }

    private void validatePassword(String password, User user) {
        boolean isPasswordCorrect = passwordEncoder.matches(password, user.getPassword());
        if (!isPasswordCorrect) {
            throw new HttpServerErrorException(HttpStatus.FORBIDDEN, "Password is not correct");
        }
    }

    public boolean isCorrectAdmin(String userName, String password) {
        User user = getUserByUserName(userName);
        return user.getPassword().equals(password)
                && user.getRole().getName().equals("ADMIN");
    }

    public List<Order> getUserOrdersSortByOrderDateReversed(String username) {
        Long userId = getUserByUserName(username).getId();
        List<Order> orders = orderRepository.findAllByUserId(userId);
        orders.sort(Comparator.comparing(Order::getOrderDate).reversed());
        return orders;
    }

    public Long addUserAndReturnId(User user) {
        User userFromRepository = getUserByUserName(user.getUserName());
        return userFromRepository == null ? userRepository.save(user).getId() : -1L;
    }

    public boolean isCorrectUser(String userName, String password) {
        User user = getUserByUserName(userName);
        if (user == null) {
            return false;
        }

        boolean isPasswordCorrect = passwordEncoder.matches(password, user.getPassword());
        return user.getUserName().equals(userName) && isPasswordCorrect;
    }

    public User getUserByUserName(String username) {
        Optional<User> optionalUser = userRepository.findByUserName(username);

        return optionalUser.orElse(null);
    }

    public User getUserDetailsById(Long userId) {
        return userRepository.getById(userId);
    }

    public User updateUserDetails(Long userId, UserDto userDto) {
        if(isEmpty(userId)){
            throw new IllegalArgumentException("userId cannot be null or empty");
        }
        
        User user = userRepository.getById(userId);
        populateDtoToUser(userDto, user);

        return userRepository.save(user);
    }

    private static void populateDtoToUser(UserDto userDto, User user) {
        user.setUserName(userDto.getUsername());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setAddress(userDto.getAddress());
        user.setPhoneNumber(userDto.getPhoneNumber());
    }
}
