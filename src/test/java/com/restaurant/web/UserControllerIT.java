package com.restaurant.web;

import com.restaurant.RestaurantApplication;
import com.restaurant.database.dao.ItemRepository;
import com.restaurant.database.dao.OrderRepository;
import com.restaurant.database.dao.UserRepository;
import com.restaurant.database.entity.Item;
import com.restaurant.database.entity.Order;
import com.restaurant.database.entity.User;
import com.restaurant.web.dto.PasswordDto;
import com.restaurant.web.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest(classes = RestaurantApplication.class)
@TestPropertySource(locations="classpath:application-test.properties")
@Sql({"classpath:db-test/test-food-and-user-data.sql"})
class UserControllerIT {
    public static final String NEW_USER_NAME = "NEW_USER_NAME";

    public static final String NEW_FIRST_NAME = "NEW_FIRST_NAME";

    public static final String NEW_LAST_NAME = "NEW_LAST_NAME";

    public static final String NEW_EMAIL = "NEW_EMAIL";

    public static final String NEW_ADDRESS = "NEW_ADDRESS";

    public static final String NEW_PHONE_NUMBER = "8904234323423";

    @Autowired
    private UserController userController;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void getUserDetailsById() {
        long userId = 1L;

        UserDto actualUserDto = userController.getUserDetailsById(userId);

        User expectedUser = userRepository.getById(userId);
        assertEquals(expectedUser.getUserName(), actualUserDto.getUsername());
        assertEquals(expectedUser.getFirstName(), actualUserDto.getFirstName());
        assertEquals(expectedUser.getLastName(), actualUserDto.getLastName());
        assertEquals(expectedUser.getEmail(), actualUserDto.getEmail());
        assertEquals(expectedUser.getRole().getName(), actualUserDto.getRole());
        assertEquals(expectedUser.getPhoneNumber(), actualUserDto.getPhoneNumber());
        assertEquals(expectedUser.getAddress(), actualUserDto.getAddress());
        assertNull(actualUserDto.getPassword());
    }

    @Test
    void updateUserDetails() {
        final long userId = 1L;
        UserDto userDto = UserDto.builder()
                .username(NEW_USER_NAME)
                .firstName(NEW_FIRST_NAME)
                .lastName(NEW_LAST_NAME)
                .email(NEW_EMAIL)
                .address(NEW_ADDRESS)
                .phoneNumber(NEW_PHONE_NUMBER)
                .build();

        UserDto response = userController.updateUserDetails(userId, userDto);

        User userFromDb = userRepository.getById(userId);
        assertEquals(NEW_USER_NAME, userFromDb.getUserName());
        assertEquals(NEW_FIRST_NAME, userFromDb.getFirstName());
        assertEquals(NEW_LAST_NAME, userFromDb.getLastName());
        assertEquals(NEW_EMAIL, userFromDb.getEmail());
        assertEquals(NEW_PHONE_NUMBER, userFromDb.getPhoneNumber());
        assertEquals(NEW_ADDRESS, userFromDb.getAddress());
        assertNotNull(userFromDb.getRole());
        assertNotNull(userFromDb.getPassword());
        assertNull(response.getPassword());
    }

    @Test
    public void updatePassword() {
        final long userId = 1L;
        @Valid PasswordDto passwordDto = new PasswordDto("password", "newPassword");

        ResponseEntity<Boolean> response = userController.updatePassword(userId, passwordDto);

        assertEquals(ResponseEntity.ok(Boolean.TRUE) ,response);
        User userFromDb = userRepository.getById(userId);
        assertTrue(passwordEncoder.matches("newPassword", userFromDb.getPassword()));
    }

    @Test
    public void getAllUsersDetails() {
        List<User> users = userRepository.findAll();

        List<UserDto> usersDetails =  userController.getAllUsersDetails();

        assertEquals(users.size(), usersDetails.size());
        usersDetails.forEach(usersDetail -> {
            User expectedUser = users.stream()
                    .filter(user -> user.getId().equals(usersDetail.getId()))
                    .findAny()
                    .orElse(null);
            assertNotNull(expectedUser);
            assertEquals(expectedUser.getUserName(), usersDetail.getUsername());
            assertEquals(expectedUser.getFirstName(), usersDetail.getFirstName());
            assertEquals(expectedUser.getLastName(), usersDetail.getLastName());
            assertEquals(expectedUser.getEmail(), usersDetail.getEmail());
            assertEquals(expectedUser.getRole().getName(), usersDetail.getRole());
            assertEquals(expectedUser.getPhoneNumber(), usersDetail.getPhoneNumber());
            assertEquals(expectedUser.getAddress(), usersDetail.getAddress());
            assertNull(usersDetail.getPassword());
        });
    }

    //TODO rewrite saving initial data in sql script
    @Test
    public void deleteUserById() {
        final long expectedUserId = 1L;
        long expectedItemToDelete = 1L;
        Optional<User> userToDelete = userRepository.findById(expectedUserId);
        assertTrue(userToDelete.isPresent());
        List<Order> orderListToDelete = orderRepository.findAllByUserId(expectedUserId);
        assertTrue(orderListToDelete.size() > 0);
        Optional<Item> itemToDelete = itemRepository.findById(expectedItemToDelete);
        assertTrue(itemToDelete.isPresent());

        String actualUserId = userController.deleteUserById(expectedUserId);

        assertEquals(String.valueOf(expectedUserId), actualUserId);
        Optional<User> deletedUser = userRepository.findById(expectedUserId);
        assertFalse(deletedUser.isPresent());
        List<Order> deletedOrderList = orderRepository.findAllByUserId(expectedUserId);
        assertEquals(0, deletedOrderList.size());
        Optional<Item> deletedItem = itemRepository.findById(expectedItemToDelete);
        assertFalse(deletedItem.isPresent());
    }
}