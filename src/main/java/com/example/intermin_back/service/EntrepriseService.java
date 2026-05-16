package com.example.intermin_back.service;

import com.example.intermin_back.model.Entreprise;
import com.example.intermin_back.repository.EntrepriseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EntrepriseService {

    private final EntrepriseRepository entrepriseRepository;

    @Transactional
    public Entreprise creerEntreprise(Entreprise entreprise) {
        return entrepriseRepository.save(entreprise);
    }
}
