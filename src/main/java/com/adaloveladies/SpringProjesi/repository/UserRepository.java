package com.adaloveladies.SpringProjesi.repository;

import com.adaloveladies.SpringProjesi.model.Kullanici;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Kullanici, Long> {
    Optional<Kullanici> findByUsername(String username);
    Optional<Kullanici> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
