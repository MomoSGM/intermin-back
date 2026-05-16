package com.example.intermin_back.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "interimaire")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Interimaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String competences;
    private String localisation;

    @Builder.Default
    private boolean disponible = false;

    @OneToMany(mappedBy = "interimaire")
    @JsonIgnore
    private List<Mission> missions;
}
