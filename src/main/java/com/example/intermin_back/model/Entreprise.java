package com.example.intermin_back.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "entreprise")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Entreprise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String email;
    private String telephone;
    private String adresse;

    @OneToMany(mappedBy = "entreprise")
    @JsonIgnore
    private List<Mission> missions;
}
