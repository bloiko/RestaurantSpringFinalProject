package com.restaurant.service;


import com.restaurant.database.dao.OrderRepository;
import com.restaurant.database.dao.RoleRepository;
import com.restaurant.database.dao.UserRepository;
import com.restaurant.database.entity.Order;
import com.restaurant.database.entity.Role;
import com.restaurant.database.entity.User;
import com.restaurant.security.jwt.JwtProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

    @Autowired
    public UserService(UserRepository userRepository, OrderRepository orderRepository, AuthenticationManager authenticationManager,
                       RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.authenticationManager = authenticationManager;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    public Optional<String> login(String username, String password) {
        log.info("New user attempting to sign in");
        Optional<String> token = Optional.empty();
        Optional<User> user = userRepository.findByUserName(username);
        if (user.isPresent()) {
            try {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
                token = Optional.of(jwtProvider.createToken(username, user.get().getRole()));
            } catch (AuthenticationException e){
                log.info("Log in failed for user {}", username);
            }
        }
        return token;
    }

    public Optional<User> register(String username, String password, String firstName, String lastName) {
        log.info("New user attempting to sign in");
        Optional<User> user = Optional.empty();
        if (!userRepository.findByUserName(username).isPresent()) {
            Optional<Role> role = roleRepository.findByName("USER");
            user = Optional.of(userRepository.save(new User(username,
                    passwordEncoder.encode(password),
                    role.get())));
        }
        return user;
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
