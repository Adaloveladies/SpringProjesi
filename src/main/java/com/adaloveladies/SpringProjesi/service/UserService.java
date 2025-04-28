package com.adaloveladies.SpringProjesi.service;

import com.adaloveladies.SpringProjesi.model.User;
import com.adaloveladies.SpringProjesi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public boolean authenticate(User user) {
        Optional<User> existingUser = userRepository.findByUsername(user.getUsername());

        if (existingUser.isPresent()) {
            return passwordEncoder.matches(user.getPassword(), existingUser.get().getPassword());
        }

        return false;
    }

    public boolean userExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public String registerUser(User user) {
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        userRepository.save(user); // Kullanıcıyı veri tabanına kaydediyoruz
        System.out.println("Kayıt: " + user.getUsername() + " - " + user.getPassword());
        return "User registered successfully";
    }

    // 📌 Eksik olan method buraya eklendi
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
