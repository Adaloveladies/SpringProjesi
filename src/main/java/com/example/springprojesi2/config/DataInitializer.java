package com.example.springprojesi2.config;

import com.example.springprojesi2.entity.Rol;
import com.example.springprojesi2.repository.RolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RolRepository rolRepository;

    @Override
    public void run(String... args) {
        // Varsayılan rolleri oluştur
        if (!rolRepository.existsByRolAdi("ROLE_USER")) {
            Rol userRole = Rol.builder()
                    .rolAdi("ROLE_USER")
                    .build();
            rolRepository.save(userRole);
        }

        if (!rolRepository.existsByRolAdi("ROLE_ADMIN")) {
            Rol adminRole = Rol.builder()
                    .rolAdi("ROLE_ADMIN")
                    .build();
            rolRepository.save(adminRole);
        }
    }
} 