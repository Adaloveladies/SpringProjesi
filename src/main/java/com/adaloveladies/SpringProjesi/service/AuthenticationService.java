package com.adaloveladies.SpringProjesi.service;

import com.adaloveladies.SpringProjesi.model.User;
import com.adaloveladies.SpringProjesi.repository.UserRepository;
import com.adaloveladies.SpringProjesi.security.JwtService; // Doğru import!
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public String register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return jwtService.generateToken(user);
    }

    public String authenticate(String username, String password) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            return jwtService.generateToken(userDetails);
        } catch (AuthenticationException e) {
            throw new RuntimeException("Geçersiz kullanıcı adı veya şifre");
        }
    }
}