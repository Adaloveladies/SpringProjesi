package com.adaloveladies.SpringProjesi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/api/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Token doğrulandı, erişim başarılı!");
    }

    @GetMapping("/api/protected")
    public ResponseEntity<String> testProtectedEndpoint() {
        // Burada kullanıcıyı almak için SecurityContextHolder'ı kullanıyoruz
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            return ResponseEntity.ok("Giriş yapan kullanıcı: " + auth.getName());
        } else {
            return ResponseEntity.status(401).body("Yetkisiz erişim.");
        }
    }
}