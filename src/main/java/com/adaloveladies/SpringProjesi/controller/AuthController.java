package com.adaloveladies.SpringProjesi.controller;

import com.adaloveladies.SpringProjesi.model.User;
import com.adaloveladies.SpringProjesi.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        String result = authenticationService.register(user);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        System.out.println("Login endpoint'e istek geldi"); // TEST için eklendi
        String result = authenticationService.authenticate(user.getUsername(), user.getPassword());
        return ResponseEntity.ok(result);
    }
}
