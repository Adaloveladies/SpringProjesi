package com.adaloveladies.SpringProjesi.controller;

import com.adaloveladies.SpringProjesi.model.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserController {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String registerUser(User user) {
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        System.out.println("Kayıt: " + user.getUsername() + " - " + user.getPassword());
        return "User registered successfully";
    }

    public boolean userExists(String username) {
        return false;
    }
}
