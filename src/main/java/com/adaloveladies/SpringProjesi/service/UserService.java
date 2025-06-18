package com.adaloveladies.SpringProjesi.service;

import com.adaloveladies.SpringProjesi.dto.UserRequestDTO;
import com.adaloveladies.SpringProjesi.dto.UserResponseDTO;
import com.adaloveladies.SpringProjesi.model.Kullanici;
import com.adaloveladies.SpringProjesi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Kullanıcı işlemlerini yöneten servis
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Kullanici findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı: " + username));
    }

    /**
     * Kullanıcı girişini doğrular
     */
    public boolean authenticate(String username, String password) {
        return userRepository.findByUsername(username)
                .map(user -> passwordEncoder.matches(password, user.getPassword()))
                .orElse(false);
    }

    /**
     * Kullanıcı adının kullanılabilir olup olmadığını kontrol eder
     */
    public boolean userExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    /**
     * Yeni kullanıcı kaydı oluşturur
     */
    public UserResponseDTO registerUser(UserRequestDTO request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Bu kullanıcı adı zaten kullanılıyor");
        }

        Kullanici kullanici = Kullanici.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .level(1)
                .points(0)
                .completedTaskCount(0)
                .active(true)
                .creationDate(LocalDateTime.now())
                .build();

        Kullanici savedKullanici = userRepository.save(kullanici);
        return convertToResponseDTO(savedKullanici);
    }

    /**
     * Tüm kullanıcıları getirir
     */
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Kullanıcı profilini getirir
     */
    public UserResponseDTO getUserProfile(String username) {
        Kullanici kullanici = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
        return convertToResponseDTO(kullanici);
    }

    /**
     * Kullanici modelini UserResponseDTO'ya dönüştürür
     */
    private UserResponseDTO convertToResponseDTO(Kullanici kullanici) {
        return UserResponseDTO.builder()
                .id(kullanici.getId())
                .username(kullanici.getUsername())
                .email(kullanici.getEmail())
                .level(kullanici.getLevel())
                .points(kullanici.getPoints())
                .completedTaskCount(kullanici.getCompletedTaskCount())
                .build();
    }
}
