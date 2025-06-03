package com.adaloveladies.SpringProjesi.service;

import com.adaloveladies.SpringProjesi.dto.LeaderboardDTO;
import com.adaloveladies.SpringProjesi.model.Kullanici;
import com.adaloveladies.SpringProjesi.repository.KullaniciRepository;
import com.adaloveladies.SpringProjesi.exception.KullaniciBulunamadiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeaderboardService {
    private final KullaniciRepository kullaniciRepository;

    public List<LeaderboardDTO> getGlobalLeaderboard() {
        List<Kullanici> kullanicilar = kullaniciRepository.findTop10ByOrderByPointsDesc();
        return mapToLeaderboardDTO(kullanicilar);
    }

    public List<LeaderboardDTO> getTopBuilders() {
        List<Kullanici> kullanicilar = kullaniciRepository.findTop10ByOrderByCompletedTaskCountDesc();
        return mapToLeaderboardDTO(kullanicilar);
    }

    public LeaderboardDTO getUserRanking(Long userId) {
        Kullanici kullanici = kullaniciRepository.findById(userId)
                .orElseThrow(() -> new KullaniciBulunamadiException("Kullanıcı bulunamadı"));
        
        long rank = kullaniciRepository.countByPointsGreaterThan(kullanici.getPoints()) + 1;
        
        return LeaderboardDTO.builder()
                .userId(kullanici.getId())
                .username(kullanici.getUsername())
                .points(kullanici.getPoints())
                .rank((int) rank)
                .completedTaskCount(kullanici.getCompletedTaskCount())
                .build();
    }

    private List<LeaderboardDTO> mapToLeaderboardDTO(List<Kullanici> kullanicilar) {
        return kullanicilar.stream()
                .map(kullanici -> LeaderboardDTO.builder()
                        .userId(kullanici.getId())
                        .username(kullanici.getUsername())
                        .points(kullanici.getPoints())
                        .rank(kullanicilar.indexOf(kullanici) + 1)
                        .completedTaskCount(kullanici.getCompletedTaskCount())
                        .build())
                .collect(Collectors.toList());
    }
} 