package com.example.intermin_back.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String email;
    private String password;
    private String role;       // "ENTREPRISE" ou "INTERIMAIRE"
    private String nom;
    // Champs spécifiques INTERIMAIRE (optionnels)
    private String localisation;
    private String competences;
}
