package com.restaurant.web;

import com.restaurant.RestaurantApplication;
import com.restaurant.database.dao.UserRepository;
import com.restaurant.database.entity.User;
import com.restaurant.web.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest(classes = RestaurantApplication.class)
@TestPropertySource(locations="classpath:application-test.properties")
@Sql({"classpath:db-test/test-user-data.sql"})
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
}