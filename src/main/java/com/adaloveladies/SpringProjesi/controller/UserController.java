package com.adaloveladies.SpringProjesi.controller;

import com.adaloveladies.SpringProjesi.model.User;
import com.adaloveladies.SpringProjesi.repository.UserRepository;
import com.adaloveladies.SpringProjesi.service.AuthenticationService;
import com.adaloveladies.SpringProjesi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;
    private final UserService userService;

    // Kullanıcı Giriş İşlemi
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        // Kullanıcı adı ve şifreyi doğrulamak için AuthenticationService'i kullanıyoruz
        String result = authenticationService.authenticate(user.getUsername(), user.getPassword());

        if (result != null) {
            return ResponseEntity.ok(result);  // JWT Token'ı geri döndür
        } else {
            return ResponseEntity.status(401).body("Geçersiz kullanıcı adı veya şifre!");
        }
    }

    // Giriş Yapmış Kullanıcının Profil Bilgilerini Getir
    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        // Giriş yapmış kullanıcının adını alıyoruz
        String username = userDetails.getUsername();

        // Kullanıcıyı veritabanından çekiyoruz
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        return ResponseEntity.ok(user);  // Kullanıcı bilgilerini geri döndürüyoruz
    }
}
