package com.adaloveladies.SpringProjesi.controller;

import com.adaloveladies.SpringProjesi.dto.LeaderboardDTO;
import com.adaloveladies.SpringProjesi.service.LeaderboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leaderboard")
@RequiredArgsConstructor
public class LeaderboardController {

    private final LeaderboardService leaderboardService;

    @GetMapping("/global")
    public ResponseEntity<List<LeaderboardDTO>> getGlobalLeaderboard() {
        return ResponseEntity.ok(leaderboardService.getGlobalLeaderboard());
    }

    @GetMapping("/builders")
    public ResponseEntity<List<LeaderboardDTO>> getTopBuilders() {
        return ResponseEntity.ok(leaderboardService.getTopBuilders());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<LeaderboardDTO> getUserRanking(@PathVariable Long userId) {
        return ResponseEntity.ok(leaderboardService.getUserRanking(userId));
    }
} 