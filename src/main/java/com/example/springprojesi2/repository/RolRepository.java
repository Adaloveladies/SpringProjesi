package com.example.springprojesi2.repository;

import com.example.springprojesi2.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol, Long> {
    Optional<Rol> findByRolAdi(String rolAdi);
    boolean existsByRolAdi(String rolAdi);
} 