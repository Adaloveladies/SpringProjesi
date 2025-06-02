package com.adaloveladies.SpringProjesi.service;

import com.adaloveladies.SpringProjesi.dto.AuthRequestDTO;
import com.adaloveladies.SpringProjesi.dto.AuthResponseDTO;
import com.adaloveladies.SpringProjesi.model.Kullanici;
import com.adaloveladies.SpringProjesi.model.Rol;
import com.adaloveladies.SpringProjesi.repository.KullaniciRepository;
import com.adaloveladies.SpringProjesi.repository.RolRepository;
import com.adaloveladies.SpringProjesi.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final KullaniciRepository kullaniciRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public AuthResponseDTO login(AuthRequestDTO request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(authentication);

        Kullanici kullanici = kullaniciRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        return AuthResponseDTO.builder()
                .token(token)
                .username(kullanici.getUsername())
                .email(kullanici.getEmail())
                .roller(kullanici.getRolAdlari())
                .build();
    }

    @Transactional
    public AuthResponseDTO register(AuthRequestDTO request) {
        if (kullaniciRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Bu kullanıcı adı zaten kullanılıyor");
        }

        if (kullaniciRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Bu email zaten kullanılıyor");
        }

        Kullanici kullanici = Kullanici.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        Set<Rol> roller = new HashSet<>();
        rolRepository.findByAd("USER").ifPresent(roller::add);
        kullanici.setRoller(roller);

        kullaniciRepository.save(kullanici);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(authentication);

        return AuthResponseDTO.builder()
                .token(token)
                .username(kullanici.getUsername())
                .email(kullanici.getEmail())
                .roller(kullanici.getRolAdlari())
                .build();
    }
} 