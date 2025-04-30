package com.adaloveladies.SpringProjesi.service;

import com.adaloveladies.SpringProjesi.model.User;
import com.adaloveladies.SpringProjesi.repository.UserRepository;
import com.adaloveladies.SpringProjesi.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    // Kullanıcı kaydı
    public String register(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Bu kullanıcı adı zaten alınmış.");
        }

        // Şifreyi hashleyip kullanıcıyı kaydet
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        // JWT token üret
        UserDetails userDetails = userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new RuntimeException("Kayıt sonrası kullanıcı alınamadı."));

        return jwtService.generateToken(userDetails);
    }

    // Giriş işlemi
    public String authenticate(String username, String password) {
        try {
            // Kimlik doğrulama
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
        } catch (AuthenticationException e) {
            throw new RuntimeException("Kullanıcı adı veya şifre hatalı.");
        }

        // Kullanıcıyı veritabanından al
        UserDetails userDetails = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı."));

        return jwtService.generateToken(userDetails);
    }
}