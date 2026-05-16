package com.example.intermin_back.service;

import com.example.intermin_back.model.Interimaire;
import com.example.intermin_back.repository.InterimaireRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InterimaireService {

    private final InterimaireRepository interimaireRepository;

    @Transactional
    public Interimaire creerInterimaire(Interimaire interimaire) {
        return interimaireRepository.save(interimaire);
    }

    public List<Interimaire> getDisponibles() {
        return interimaireRepository.findByDisponibleTrue();
    }
}
