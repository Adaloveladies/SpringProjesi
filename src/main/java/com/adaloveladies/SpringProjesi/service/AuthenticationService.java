package com.adaloveladies.SpringProjesi.service;

import com.adaloveladies.SpringProjesi.model.Kullanici;
import com.adaloveladies.SpringProjesi.repository.KullaniciRepository;
import com.adaloveladies.SpringProjesi.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final KullaniciRepository kullaniciRepository;

    @Transactional
    public String authenticate(String username, String password) {
        try {
            // Kullanıcının varlığını kontrol et
            Kullanici kullanici = kullaniciRepository.findByUsername(username)
                    .orElseThrow(() -> new BadCredentialsException("Geçersiz kullanıcı adı veya şifre"));

            // Hesabın aktif olup olmadığını kontrol et
            if (!kullanici.isActive()) {
                throw new BadCredentialsException("Hesabınız devre dışı bırakılmış");
            }

            // Kimlik doğrulama işlemini gerçekleştir
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            // Güvenlik bağlamını ayarla
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // JWT token oluştur ve döndür
            return jwtTokenProvider.generateToken(authentication);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Geçersiz kullanıcı adı veya şifre");
        }
    }
}