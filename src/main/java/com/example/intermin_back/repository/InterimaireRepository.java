package com.example.intermin_back.repository;

import com.example.intermin_back.model.Interimaire;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterimaireRepository extends JpaRepository<Interimaire, Long> {
    List<Interimaire> findByDisponibleTrue();
}
