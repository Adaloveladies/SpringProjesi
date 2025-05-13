package com.adaloveladies.SpringProjesi.controller;

import com.adaloveladies.SpringProjesi.dto.UserRequestDTO;
import com.adaloveladies.SpringProjesi.dto.UserResponseDTO;
import com.adaloveladies.SpringProjesi.service.AuthenticationService;
import com.adaloveladies.SpringProjesi.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Kullanıcı işlemleri için REST API endpoint'lerini sağlayan controller
 */
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    /**
     * Kullanıcı giriş işlemi
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody UserRequestDTO loginRequest) {
        String token = authenticationService.authenticate(
                loginRequest.getUsername(), 
                loginRequest.getPassword()
        );

        if (token != null) {
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(401).body("Geçersiz kullanıcı adı veya şifre!");
        }
    }

    /**
     * Yeni kullanıcı kaydı
     */
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody UserRequestDTO registerRequest) {
        UserResponseDTO user = userService.registerUser(registerRequest);
        return ResponseEntity.ok(user);
    }

    /**
     * Giriş yapmış kullanıcının profil bilgilerini getirir
     */
    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getCurrentUser(
            @AuthenticationPrincipal UserDetails userDetails) {
        UserResponseDTO userDTO = userService.getUserProfile(userDetails.getUsername());
        return ResponseEntity.ok(userDTO);
    }

    /**
     * Tüm kullanıcıları getirir
     */
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}
