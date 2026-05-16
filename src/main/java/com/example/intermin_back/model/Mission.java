package com.example.intermin_back.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "mission")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String poste;
    private String localisation;
    private int duree;

    @Builder.Default
    private String statut = "PUBLIEE";

    private LocalDate dateDebut;
    private LocalDate dateFin;

    @ManyToOne
    @JoinColumn(name = "id_entreprise", nullable = false)
    private Entreprise entreprise;

    @ManyToOne
    @JoinColumn(name = "id_interimaire")
    private Interimaire interimaire;
}
