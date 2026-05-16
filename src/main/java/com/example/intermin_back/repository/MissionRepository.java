package com.example.intermin_back.repository;

import com.example.intermin_back.model.Mission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MissionRepository extends JpaRepository<Mission, Long> {
    List<Mission> findByStatut(String statut);
    // Récupère toutes les missions d'une entreprise (PUBLIEE + ACCEPTEE)
    List<Mission> findByEntrepriseId(Long entrepriseId);
    List<Mission> findByInterimaireId(Long interimaireId);
}
