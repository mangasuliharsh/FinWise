package com.finwise.controller;

import com.finwise.entity.User;
import com.finwise.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public ResponseEntity<User> getUser() {
        return userService.getUser()
                .map(user -> new ResponseEntity<>(user,HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUserId(@PathVariable Long id) {
        return new ResponseEntity<>(userService.getUserById(id),HttpStatus.OK);
    }
}
