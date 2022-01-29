package com.restaurant.web;

import com.restaurant.database.entity.User;
import com.restaurant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {
    @Autowired
    private UserService userService;

    @PostMapping("/registration1")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        userService.addUserAndReturnId(user);

        return ResponseEntity.ok().build();
    }
}
