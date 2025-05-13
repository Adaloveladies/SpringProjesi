package com.adaloveladies.SpringProjesi.service;

import com.adaloveladies.SpringProjesi.dto.UserRequestDTO;
import com.adaloveladies.SpringProjesi.dto.UserResponseDTO;
import com.adaloveladies.SpringProjesi.model.User;
import com.adaloveladies.SpringProjesi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Kullanıcı işlemlerini yöneten servis
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

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
        if (userExists(request.getUsername())) {
            throw new RuntimeException("Bu kullanıcı adı zaten kullanılıyor");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .score(0)
                .level(1)
                .build();

        User savedUser = userRepository.save(user);
        return convertToResponseDTO(savedUser);
    }

    /**
     * Tüm kullanıcıları getirir
     */
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    /**
     * Kullanıcı profilini getirir
     */
    public UserResponseDTO getUserProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
        return convertToResponseDTO(user);
    }

    /**
     * User modelini UserResponseDTO'ya dönüştürür
     */
    private UserResponseDTO convertToResponseDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .score(user.getScore())
                .level(user.getLevel())
                .build();
    }
}
