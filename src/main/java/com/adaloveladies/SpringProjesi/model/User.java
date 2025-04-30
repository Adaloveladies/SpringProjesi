package com.adaloveladies.SpringProjesi.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

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