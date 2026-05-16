package com.example.intermin_back.controller;

import com.example.intermin_back.model.Interimaire;
import com.example.intermin_back.service.InterimaireService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interimaires")
@RequiredArgsConstructor
public class InterimaireController {

    private final InterimaireService interimaireService;

    @PostMapping
    public ResponseEntity<Interimaire> creerInterimaire(@RequestBody Interimaire interimaire) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(interimaireService.creerInterimaire(interimaire));
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<Interimaire>> getDisponibles() {
        return ResponseEntity.ok(interimaireService.getDisponibles());
    }
}
