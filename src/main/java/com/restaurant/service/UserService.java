package com.restaurant.service;


import com.restaurant.database.dao.OrderRepository;
import com.restaurant.database.dao.RoleRepository;
import com.restaurant.database.dao.UserRepository;
import com.restaurant.database.entity.Order;
import com.restaurant.database.entity.Role;
import com.restaurant.database.entity.User;
import com.restaurant.security.jwt.JwtProvider;
import com.restaurant.web.dto.RegistrationRequest;
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
        Optional<String> token = Optional.empty();
        Optional<User> user = userRepository.findByUserName(username);
        if (!user.isPresent()) {
            throw new HttpServerErrorException(HttpStatus.FORBIDDEN, "Unknown user");
        }

        boolean isPasswordCorrect = passwordEncoder.matches(password, user.get().getPassword());
        if (isPasswordCorrect) {
            try {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
                token = Optional.of(jwtProvider.createToken(username, user.get().getRole()));
            } catch (AuthenticationException e) {
                log.info("Log in failed for user {}", username);
            }
        }
        return token.orElseThrow(() -> new HttpServerErrorException(HttpStatus.FORBIDDEN, "Password is not correct"));
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

    public boolean isCorrectAdmin(String userName, String password) {
        User user = getUserByUserName(userName);
        return user != null && user.getPassword().equals(password)
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

        return user != null && user.getUserName().equals(userName) && user.getPassword().equals(password)
                && !user.getRole().getName().equals(null);
    }

    public User getUserByUserName(String username) {
        Optional<User> optional = userRepository.findByUserName(username);
        return optional.orElse(null);
    }
}
