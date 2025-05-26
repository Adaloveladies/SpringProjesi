package com.adaloveladies.SpringProjesi.service;

import com.adaloveladies.SpringProjesi.dto.LeaderboardDTO;
import com.adaloveladies.SpringProjesi.model.User;
import com.adaloveladies.SpringProjesi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeaderboardService {

    private final UserRepository userRepository;

    public List<LeaderboardDTO> getLeaderboard() {
        return userRepository.findAll().stream()
                .map(this::convertToLeaderboardDTO)
                .sorted((a, b) -> {
                    // Önce puanlara göre sırala
                    int scoreCompare = b.getScore().compareTo(a.getScore());
                    if (scoreCompare != 0) {
                        return scoreCompare;
                    }
                    // Puanlar eşitse seviyeye göre sırala
                    int levelCompare = b.getLevel().compareTo(a.getLevel());
                    if (levelCompare != 0) {
                        return levelCompare;
                    }
                    // Seviyeler de eşitse tamamlanan bina sayısına göre sırala
                    return b.getCompletedBuildings().compareTo(a.getCompletedBuildings());
                })
                .collect(Collectors.toList());
    }

    public List<LeaderboardDTO> getTopBuilders() {
        return userRepository.findAll().stream()
                .map(this::convertToLeaderboardDTO)
                .sorted((a, b) -> b.getCompletedBuildings().compareTo(a.getCompletedBuildings()))
                .collect(Collectors.toList());
    }

    public LeaderboardDTO getUserRanking(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
        return convertToLeaderboardDTO(user);
    }

    private LeaderboardDTO convertToLeaderboardDTO(User user) {
        return LeaderboardDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .score(user.getScore())
                .level(user.getLevel())
                .completedBuildings(user.getCompletedBuildings())
                .build();
    }
} 