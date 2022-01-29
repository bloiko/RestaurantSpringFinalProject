package com.restaurant.service;


import com.restaurant.database.dao.OrderRepository;
import com.restaurant.database.dao.UserRepository;
import com.restaurant.database.entity.Order;
import com.restaurant.database.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
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
@Service
public class UserService {
    private final UserRepository userRepository;

    private final OrderRepository orderRepository;

    @Autowired
    public UserService(UserRepository userRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public boolean isCorrectAdmin(String userName, String password) {
        User user = getUserByUserName(userName);
        return user != null && user.getPassword().equals(password)
                && user.getRole().getName().equals("ADMIN");
    }

    @Transactional
    public List<Order> getUserOrdersSortByOrderDateReversed(String username) {
        Long userId = getUserByUserName(username).getId();
        List<Order> orders = orderRepository.findAllByUserId(userId);
        orders.sort(Comparator.comparing(Order::getOrderDate).reversed());
        return orders;
    }

    @Transactional
    public Long addUserAndReturnId(User user) {
        User userFromRepository = getUserByUserName(user.getUserName());
        return userFromRepository == null ? userRepository.save(user).getId() : -1L;
    }

    @Transactional
    public boolean isCorrectUser(String userName, String password) {
        User user = getUserByUserName(userName);

        return user != null && user.getUserName().equals(userName) && user.getPassword().equals(password)
                && !user.getRole().getName().equals(null);
    }

    @Transactional
    public User getUserByUserName(String username) {
        Optional<User> optional = userRepository.findByUserName(username);
        return optional.orElse(null);
    }
}
