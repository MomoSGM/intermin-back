package com.example.intermin_back.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String role;
    private String nom;
    // Id de l'entité métier liée — utilisé côté frontend pour les actions (ex: accepter une mission)
    private Long entityId;
}
