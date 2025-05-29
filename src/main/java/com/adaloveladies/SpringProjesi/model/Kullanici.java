package com.adaloveladies.SpringProjesi.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "kullanicilar", indexes = {
    @Index(name = "idx_kullanici_email", columnList = "email", unique = true),
    @Index(name = "idx_kullanici_kullanici_adi", columnList = "kullanici_adi", unique = true),
    @Index(name = "idx_kullanici_seviye", columnList = "seviye"),
    @Index(name = "idx_kullanici_puan", columnList = "puan")
})
public class Kullanici implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "kullanici_adi", nullable = false, unique = true, length = 50)
    @ToString.Include
    @EqualsAndHashCode.Include
    private String kullaniciAdi;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(length = 50)
    private String ad;

    @Column(length = 50)
    private String soyad;

    @Column(nullable = false)
    @Builder.Default
    private Integer seviye = 1;

    @Column(nullable = false)
    @Builder.Default
    private Integer puan = 0;

    @Column(name = "olusturma_tarihi", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime olusturmaTarihi = LocalDateTime.now();

    @Column(name = "son_giris_tarihi")
    private LocalDateTime sonGirisTarihi;

    @Column(name = "son_guncelleme_tarihi")
    @Builder.Default
    private LocalDateTime sonGuncellemeTarihi = LocalDateTime.now();

    @Column(name = "hesap_aktif", nullable = false)
    @Builder.Default
    private boolean hesapAktif = true;

    @Column(name = "hesap_kilitli", nullable = false)
    @Builder.Default
    private boolean hesapKilitli = false;

    @OneToMany(mappedBy = "kullanici", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Gorev> gorevler = new HashSet<>();

    @OneToMany(mappedBy = "kullanici", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Rozet> rozetler = new HashSet<>();

    @OneToMany(mappedBy = "kullanici", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Bildirim> bildirimler = new HashSet<>();

    @OneToOne(mappedBy = "kullanici", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Istatistik istatistik;

    @PrePersist
    protected void onCreate() {
        if (olusturmaTarihi == null) {
            olusturmaTarihi = LocalDateTime.now();
        }
        if (sonGuncellemeTarihi == null) {
            sonGuncellemeTarihi = LocalDateTime.now();
        }
        if (puan == null) {
            puan = 0;
        }
        if (seviye == null) {
            seviye = 1;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        sonGuncellemeTarihi = LocalDateTime.now();
    }

    public void puanEkle(int eklenecekPuan) {
        if (eklenecekPuan < 0) {
            throw new IllegalArgumentException("Eklenecek puan negatif olamaz");
        }
        this.puan += eklenecekPuan;
        seviyeKontrol();
    }

    private void seviyeKontrol() {
        int yeniSeviye = (puan / 100) + 1;
        if (yeniSeviye > seviye) {
            this.seviye = yeniSeviye;
        }
    }

    public void gorevEkle(Gorev gorev) {
        if (gorev == null) {
            throw new IllegalArgumentException("Görev null olamaz");
        }
        gorevler.add(gorev);
        gorev.setKullanici(this);
    }

    public void gorevSil(Gorev gorev) {
        if (gorev == null) {
            throw new IllegalArgumentException("Görev null olamaz");
        }
        gorevler.remove(gorev);
        gorev.setKullanici(null);
    }

    public void rozetEkle(Rozet rozet) {
        if (rozet == null) {
            throw new IllegalArgumentException("Rozet null olamaz");
        }
        rozetler.add(rozet);
        rozet.setKullanici(this);
    }

    public void rozetSil(Rozet rozet) {
        if (rozet == null) {
            throw new IllegalArgumentException("Rozet null olamaz");
        }
        rozetler.remove(rozet);
        rozet.setKullanici(null);
    }

    public void bildirimEkle(Bildirim bildirim) {
        if (bildirim == null) {
            throw new IllegalArgumentException("Bildirim null olamaz");
        }
        bildirimler.add(bildirim);
        bildirim.setKullanici(this);
    }

    public void bildirimSil(Bildirim bildirim) {
        if (bildirim == null) {
            throw new IllegalArgumentException("Bildirim null olamaz");
        }
        bildirimler.remove(bildirim);
        bildirim.setKullanici(null);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return kullaniciAdi;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !hesapKilitli;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return hesapAktif;
    }
} 