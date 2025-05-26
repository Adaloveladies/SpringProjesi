package com.adaloveladies.SpringProjesi.controller;

import com.adaloveladies.SpringProjesi.dto.AuthResponseDTO;
import com.adaloveladies.SpringProjesi.dto.LoginRequestDTO;
import com.adaloveladies.SpringProjesi.dto.RegisterRequestDTO;
import com.adaloveladies.SpringProjesi.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody RegisterRequestDTO request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
