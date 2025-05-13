package com.adaloveladies.SpringProjesi.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Kullanıcı modeli
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "score", nullable = false)
    private Integer score = 0; // Kullanıcının puanı

    @Column(name = "level", nullable = false)
    private Integer level = 1; // Kullanıcının seviyesi

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Task> tasks;

    // UserDetails metotları:
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Eğer kullanıcıya roller eklemek istersen, burada ilgili authorities dönebilirsin
        return Collections.emptyList(); // Şu an için boş, fakat ileride roller ekleyebilirsin
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Hesap süresi dolmuşsa false dönebilirsin
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Hesap kilitliyse false dönebilirsin
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Şifre süresi dolmuşsa false dönebilirsin
    }

    @Override
    public boolean isEnabled() {
        return true; // Hesap etkin değilse false dönebilirsin
    }
}