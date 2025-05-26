package com.adaloveladies.SpringProjesi.service;

import com.adaloveladies.SpringProjesi.dto.AuthResponseDTO;
import com.adaloveladies.SpringProjesi.dto.LoginRequestDTO;
import com.adaloveladies.SpringProjesi.dto.RegisterRequestDTO;
import com.adaloveladies.SpringProjesi.model.User;
import com.adaloveladies.SpringProjesi.repository.UserRepository;
import com.adaloveladies.SpringProjesi.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    public AuthResponseDTO register(RegisterRequestDTO request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Kullanıcı adı zaten kullanımda");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email zaten kullanımda");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        String token = jwtTokenProvider.generateToken(user);
        return new AuthResponseDTO(token, user.getUsername(), user.getEmail());
    }

    public AuthResponseDTO login(LoginRequestDTO request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User user = (User) authentication.getPrincipal();
        String token = jwtTokenProvider.generateToken(user);
        return new AuthResponseDTO(token, user.getUsername(), user.getEmail());
    }
} 