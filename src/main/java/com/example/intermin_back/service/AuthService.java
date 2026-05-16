package com.example.intermin_back.service;

import com.example.intermin_back.dto.AuthResponse;
import com.example.intermin_back.dto.LoginRequest;
import com.example.intermin_back.dto.RegisterRequest;
import com.example.intermin_back.model.Entreprise;
import com.example.intermin_back.model.Interimaire;
import com.example.intermin_back.model.User;
import com.example.intermin_back.repository.EntrepriseRepository;
import com.example.intermin_back.repository.InterimaireRepository;
import com.example.intermin_back.repository.UserRepository;
import com.example.intermin_back.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final EntrepriseRepository entrepriseRepository;
    private final InterimaireRepository interimaireRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public AuthResponse register(RegisterRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email déjà utilisé");
        }

        User.UserBuilder userBuilder = User.builder()
                .email(req.getEmail())
                // BCrypt hash le mot de passe avant de le stocker — jamais en clair en base
                .password(passwordEncoder.encode(req.getPassword()))
                .role(req.getRole());

        String nom;

        if ("ENTREPRISE".equals(req.getRole())) {
            // Crée l'entité Entreprise liée au compte
            Entreprise entreprise = entrepriseRepository.save(
                    Entreprise.builder().nom(req.getNom()).email(req.getEmail()).build()
            );
            userBuilder.entreprise(entreprise);
            nom = entreprise.getNom();
        } else if ("INTERIMAIRE".equals(req.getRole())) {
            // Crée l'entité Intérimaire liée au compte
            Interimaire interimaire = interimaireRepository.save(
                    Interimaire.builder()
                            .nom(req.getNom())
                            .localisation(req.getLocalisation())
                            .competences(req.getCompetences())
                            .disponible(true)
                            .build()
            );
            userBuilder.interimaire(interimaire);
            nom = interimaire.getNom();
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rôle invalide");
        }

        User saved = userRepository.save(userBuilder.build());

        String token = jwtTokenProvider.generateToken(req.getEmail(), req.getRole());
        Long entityId = saved.getEntreprise() != null
                ? saved.getEntreprise().getId()
                : (saved.getInterimaire() != null ? saved.getInterimaire().getId() : null);
        return new AuthResponse(token, req.getRole(), nom, entityId);
    }

    public AuthResponse login(LoginRequest req) {
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email ou mot de passe incorrect"));

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email ou mot de passe incorrect");
        }

        String nom = user.getRole().equals("ENTREPRISE")
                ? (user.getEntreprise() != null ? user.getEntreprise().getNom() : user.getEmail())
                : (user.getInterimaire() != null ? user.getInterimaire().getNom() : user.getEmail());

        Long entityId = user.getEntreprise() != null
                ? user.getEntreprise().getId()
                : (user.getInterimaire() != null ? user.getInterimaire().getId() : null);

        String token = jwtTokenProvider.generateToken(user.getEmail(), user.getRole());
        return new AuthResponse(token, user.getRole(), nom, entityId);
    }
}
