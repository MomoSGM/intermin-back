package com.example.intermin_back.controller;

import com.example.intermin_back.model.Mission;
import com.example.intermin_back.service.MissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/missions")
@RequiredArgsConstructor
public class MissionController {

    private final MissionService missionService;

    @PostMapping
    public ResponseEntity<Mission> creerMission(@RequestBody Map<String, Object> payload) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(missionService.creerMission(payload));
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<Mission>> getMissionsDisponibles() {
        return ResponseEntity.ok(missionService.getMissionsDisponibles());
    }

    @GetMapping("/mes-missions")
    public ResponseEntity<List<Mission>> getMesMissions() {
        return ResponseEntity.ok(missionService.getMesMissions());
    }

    @PutMapping("/{missionId}")
    public ResponseEntity<Mission> modifierMission(
            @PathVariable Long missionId,
            @RequestBody Map<String, Object> payload) {
        return ResponseEntity.ok(missionService.modifierMission(missionId, payload));
    }

    @DeleteMapping("/{missionId}")
    public ResponseEntity<Void> supprimerMission(@PathVariable Long missionId) {
        missionService.supprimerMission(missionId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/mon-historique")
    public ResponseEntity<List<Mission>> getMonHistorique() {
        return ResponseEntity.ok(missionService.getMonHistorique());
    }

    @PutMapping("/{missionId}/annuler")
    public ResponseEntity<Mission> annulerMission(@PathVariable Long missionId) {
        return ResponseEntity.ok(missionService.annulerMission(missionId));
    }

    @PutMapping("/{missionId}/accepter/{interimaireId}")
    public ResponseEntity<Mission> accepterMission(
            @PathVariable Long missionId,
            @PathVariable Long interimaireId) {
        return ResponseEntity.ok(missionService.accepterMission(missionId, interimaireId));
    }
}
