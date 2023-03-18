package com.restaurant.service;


import com.restaurant.database.dao.OrderRepository;
import com.restaurant.database.dao.RoleRepository;
import com.restaurant.database.dao.UserRepository;
import com.restaurant.database.entity.FoodItem;
import com.restaurant.database.entity.Order;
import com.restaurant.database.entity.Role;
import com.restaurant.database.entity.User;
import com.restaurant.security.jwt.JwtProvider;
import com.restaurant.web.dto.PasswordDto;
import com.restaurant.web.dto.RegistrationRequest;
import com.restaurant.web.dto.UserDto;
import com.restaurant.web.dto.UsersPage;
import com.restaurant.web.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private static final String DEFAULT_SORT_BY_FILTER = "userName";
    private static final String ALL = "ALL";
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

    public String register(RegistrationRequest registrationRequest) {
        User registeredUserInDb = registerUserInDb(registrationRequest);

        try {
            emailService.sendSuccessfulRegistrationEmail(registrationRequest);
        } catch (MessagingException | IOException e) {
            log.error("Cannot send email to user with email = {}", registeredUserInDb.getEmail());
        }

        if (registeredUserInDb == null){
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
        }

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(registrationRequest.getUsername(), registrationRequest.getPassword()));
            return jwtProvider.createToken(registeredUserInDb.getUserName(), registeredUserInDb.getRole());
        } catch (AuthenticationException e) {
            log.info("Failed to authenticate after registration", e);
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error during authentication");
        }
    }

    private User registerUserInDb(RegistrationRequest registrationRequest) {
        log.info("New optionalUser attempting to sign in");
        Optional<User> optionalUser = userRepository.findByUserName(registrationRequest.getUsername());

        if (optionalUser.isPresent()) {
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

    public Optional<User> getUserDetailsById(Long userId) {
        return userRepository.findById(userId);
    }

    public User updateUserDetails(Long userId, UserDto userDto) {
        if(isEmpty(userId)){
            throw new IllegalArgumentException("userId cannot be null or empty");
        }
        
        Optional<User> optionalUser = userRepository.findById(userId);

        if(!optionalUser.isPresent()){
            throw new ResourceNotFoundException("User not found");
        }

        populateDtoToUser(userDto, optionalUser.get());

        return userRepository.save(optionalUser.get());
    }

    private static void populateDtoToUser(UserDto userDto, User user) {
        user.setUserName(userDto.getUsername());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setAddress(userDto.getAddress());
        user.setPhoneNumber(userDto.getPhoneNumber());
    }

    public void updatePassword(Long userId, PasswordDto passwordDto) {
        if(isEmpty(userId)){
            throw new IllegalArgumentException("userId cannot be null or empty");
        }

        User user = userRepository.getById(userId);

        validateEqualityOfPasswords(passwordDto.getOldPassword(), user.getPassword());

        user.setPassword(passwordEncoder.encode(passwordDto.getNewPassword()));
        userRepository.save(user);
    }

    private void validateEqualityOfPasswords(String oldPassword, String currentPassword) {
        boolean isOldPasswordCorrect = passwordEncoder.matches(oldPassword, currentPassword);

        if(!isOldPasswordCorrect){
            throw new IllegalArgumentException("Old password is not correct");
        }
    }

    public String deleteUserById(Long userId) {
        if(isEmpty(userId)){
            throw new IllegalArgumentException("User id is incorrect");
        }

        Optional<User> userOptional = userRepository.findById(userId);

        if(!userOptional.isPresent()){
            throw new IllegalArgumentException("User is not present in db with specified id");
        }

        orderRepository.deleteAllByUserId(userOptional.get().getId());
        userOptional.ifPresent(userRepository::delete);

        return String.valueOf(userId);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Page<User> getAllUsersByPage(UsersPage usersPage) {
        Sort sort = getSortFromDto(usersPage);

        Pageable pageable = PageRequest.of(usersPage.getPageNumber() - 1, usersPage.getPageSize(), sort);

        return getAllUsersByPageable(pageable);
    }

    private Sort getSortFromDto(UsersPage usersPage) {
        String sortBy = usersPage.getSortBy() != null ? usersPage.getSortBy() : DEFAULT_SORT_BY_FILTER;

        return Sort.by(usersPage.getSortDirection(), sortBy);
    }

    private Page<User> getAllUsersByPageable(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
}
