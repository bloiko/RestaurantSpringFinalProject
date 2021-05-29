package com.restaurant.service;


import com.restaurant.database.dao.OrderRepository;
import com.restaurant.database.dao.UserRepository;
import com.restaurant.database.entity.Order;
import com.restaurant.database.entity.Role;
import com.restaurant.database.entity.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User service.
 *
 * @author B.Loiko
 */
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
    public boolean isCorrectAdmin(String userName, String password) {
        User user = userRepository.findByUserName(userName).get();
        return user != null && user.getUserName().equals(userName) && user.getPassword().equals(password)
                && user.getRole().equals(Role.ADMIN);
    }

    public List<Order> getUserOrdersSortByOrderDateReversed(String username) {
        Long userId = userRepository.findByUserName(username).get().getId();
        List<Order> orders = orderListDAO.findAllByUserId(userId);
        orders.sort(Comparator.comparing(Order::getOrderDate).reversed());
        return orders;
    }

    public void addUserAndReturnId(User user)  {
        Long userId = userRepository.findByUserName(user.getUserName()).get().getId();
        if (userId == -1) {
            userRepository.save(user).getId();
        }
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

    public boolean isCorrectUser(String userName, String password) {

       User  user = userRepository.findByUserName(userName).get();
        return user != null && user.getUserName().equals(userName) && user.getPassword().equals(password)
                && (user.getRole().equals(Role.USER) || user.getRole().equals(Role.ADMIN));
    }

    public User getUserByUserName(String username) {
        return userRepository.findByUserName(username).get();
    }
}
