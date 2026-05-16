package com.example.intermin_back.controller;

import com.example.intermin_back.model.Entreprise;
import com.example.intermin_back.service.EntrepriseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/entreprises")
@RequiredArgsConstructor
public class EntrepriseController {

    private final EntrepriseService entrepriseService;

    @PostMapping
    public ResponseEntity<Entreprise> creerEntreprise(@RequestBody Entreprise entreprise) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(entrepriseService.creerEntreprise(entreprise));
    }
}
