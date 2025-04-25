package com.adaloveladies.SpringProjesi.controller;

import com.adaloveladies.SpringProjesi.model.User;

import com.adaloveladies.SpringProjesi.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController

@RequestMapping("/api/auth")

public class UserController {

    @Autowired

    private UserService userService;



    @PostMapping("/login")

    public ResponseEntity<String> login(@RequestBody User user) {

        boolean isAuthenticated = userService.authenticate(user);



        if (isAuthenticated) {

            return ResponseEntity.ok("Giriş başarılı!");

        } else {

            return ResponseEntity.status(401).body("Geçersiz kullanıcı adı veya şifre!");

        }

    }

}