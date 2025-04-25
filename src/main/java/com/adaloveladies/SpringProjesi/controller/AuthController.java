package com.adaloveladies.SpringProjesi.controller;

import com.adaloveladies.SpringProjesi.model.User;
import com.adaloveladies.SpringProjesi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        return userService.registerUser(user);
    }
}