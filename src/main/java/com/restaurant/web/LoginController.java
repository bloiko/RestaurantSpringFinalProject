package com.restaurant.web;

import com.restaurant.service.UserService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    @Autowired
    private UserService userService;

    @PostMapping("/login2")
    public ResponseEntity<String> execute(@PathVariable String username, @PathVariable String password){
        if (userService.isCorrectUser(username, password)) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }


    @PostMapping("/login1")
    public ResponseEntity<String> execute(@RequestBody LoginRequest loginRequest){
        if (userService.isCorrectUser(loginRequest.getUsername(), loginRequest.getPassword())) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }

    @Getter
    @Setter
    private class LoginRequest {
        private String username;

        private String password;
    }
}
