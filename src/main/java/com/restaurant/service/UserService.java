package com.restaurant.service;


import com.restaurant.database.dao.OrderRepository;
import com.restaurant.database.dao.UserRepository;
import com.restaurant.database.entity.Order;
import com.restaurant.database.entity.Role;
import com.restaurant.database.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User service.
 *
 * @author B.Loiko
 */
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderListDAO;

    /**
     * Check if user with username and password is admin
     *
     * @param userName user name
     * @param password user password.
     * @return boolean if user is admin.
     */
    @Transactional
    public boolean isCorrectAdmin(String userName, String password) {
        User user = userRepository.findByUserName(userName).get();
        return user != null && user.getUserName().equals(userName) && user.getPassword().equals(password)
                && user.getRole().equals("ADMIN");
    }
    @Transactional
    public List<Order> getUserOrdersSortByOrderDateReversed(String username) {
        Long userId = userRepository.findByUserName(username).get().getId();
        List<Order> orders = orderListDAO.findAllByUserId(userId);
        orders.sort(Comparator.comparing(Order::getOrderDate).reversed());
        return orders;
    }
    @Transactional
    public Long addUserAndReturnId(User user)  {
        Optional<User> optional = userRepository.findByUserName(user.getUserName());
        Long userId = optional.isPresent() ? optional.get().getId():-1;
        if (userId == -1) {
           return  userRepository.save(user).getId();
        }
        return -1L;
    }

    public boolean isCorrectPhoneNumber(String phoneNumber) {
        String patterns = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$"
                + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?){2}\\d{3}$"
                + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}$";
        Pattern pattern = Pattern.compile(patterns);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    public boolean isCorrectEmail(String email) {
        String patterns = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(patterns);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    @Transactional
    public boolean isCorrectUser(String userName, String password) {

       User  user = userRepository.findByUserName(userName).get();
        return user != null && user.getUserName().equals(userName) && user.getPassword().equals(password)
                && (user.getRole().getName().equals("USER") ||
                user.getRole().getName().equals("ADMIN"));
    }
    @Transactional
    public User getUserByUserName(String username) {
        Optional<User> optional = userRepository.findByUserName(username);
        return optional.isPresent()?optional.get():null;
    }
}
