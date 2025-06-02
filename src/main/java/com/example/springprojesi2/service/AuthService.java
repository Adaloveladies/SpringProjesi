package com.example.springprojesi2.service;

import com.example.springprojesi2.dto.AuthRequestDTO;
import com.example.springprojesi2.dto.AuthResponseDTO;
import com.example.springprojesi2.dto.RegisterRequestDTO;
import com.example.springprojesi2.entity.Rol;
import com.example.springprojesi2.entity.User;
import com.example.springprojesi2.repository.RolRepository;
import com.example.springprojesi2.repository.UserRepository;
import com.example.springprojesi2.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;

    public AuthResponseDTO register(RegisterRequestDTO request) {
        // Kullanıcı adı kontrolü
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Bu kullanıcı adı zaten kullanılıyor");
        }

        // Email kontrolü
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Bu email adresi zaten kullanılıyor");
        }

        // Varsayılan rolü bul
        Rol userRole = rolRepository.findByRolAdi("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Varsayılan rol bulunamadı"));

        // Kullanıcıyı oluştur
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(new HashSet<>(Set.of(userRole)))
                .build();

        userRepository.save(user);

        // UserDetails oluştur
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());

        // JWT token oluştur
        String token = jwtService.generateToken(userDetails);

        return AuthResponseDTO.builder()
                .token(token)
                .build();
    }

    public AuthResponseDTO login(AuthRequestDTO request) {
        // Kimlik doğrulama
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // UserDetails oluştur
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

        // JWT token oluştur
        String token = jwtService.generateToken(userDetails);

        return AuthResponseDTO.builder()
                .token(token)
                .build();
    }
} 