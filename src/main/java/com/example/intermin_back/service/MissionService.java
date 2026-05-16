package com.example.intermin_back.service;

import com.example.intermin_back.model.Entreprise;
import com.example.intermin_back.model.Interimaire;
import com.example.intermin_back.model.Mission;
import com.example.intermin_back.model.User;
import com.example.intermin_back.repository.EntrepriseRepository;
import com.example.intermin_back.repository.InterimaireRepository;
import com.example.intermin_back.repository.MissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MissionService {

    private final MissionRepository missionRepository;
    private final EntrepriseRepository entrepriseRepository;
    private final InterimaireRepository interimaireRepository;

    @Transactional
    public Mission creerMission(Map<String, Object> payload) {
        Long idEntreprise = Long.valueOf(payload.get("idEntreprise").toString());
        Entreprise entreprise = entrepriseRepository.findById(idEntreprise)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Entreprise non trouvée"));

        Mission mission = Mission.builder()
                .poste((String) payload.get("poste"))
                .localisation((String) payload.get("localisation"))
                .duree(Integer.parseInt(payload.get("duree").toString()))
                .entreprise(entreprise)
                .build();

        return missionRepository.save(mission);
    }

    public List<Mission> getMissionsDisponibles() {
        return missionRepository.findByStatut("PUBLIEE");
    }

    // Retourne toutes les missions de l'entreprise connectée (lues depuis le JWT)
    public List<Mission> getMesMissions() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.getEntreprise() == null)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Réservé aux entreprises");
        return missionRepository.findByEntrepriseId(user.getEntreprise().getId());
    }

    @Transactional
    public Mission modifierMission(Long missionId, Map<String, Object> payload) {
        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mission non trouvée"));

        if (payload.containsKey("poste"))
            mission.setPoste((String) payload.get("poste"));
        if (payload.containsKey("localisation"))
            mission.setLocalisation((String) payload.get("localisation"));
        if (payload.containsKey("duree"))
            mission.setDuree(Integer.parseInt(payload.get("duree").toString()));

        return missionRepository.save(mission);
    }

    @Transactional
    public void supprimerMission(Long missionId) {
        if (!missionRepository.existsById(missionId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Mission non trouvée");
        missionRepository.deleteById(missionId);
    }

    // Retourne les missions acceptées/annulées de l'intérimaire connecté
    public List<Mission> getMonHistorique() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.getInterimaire() == null)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Réservé aux intérimaires");
        return missionRepository.findByInterimaireId(user.getInterimaire().getId());
    }

    @Transactional
    public Mission annulerMission(Long missionId) {
        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mission non trouvée"));

        // Si un intérimaire était lié, on le remet disponible
        if (mission.getInterimaire() != null) {
            Interimaire interimaire = mission.getInterimaire();
            interimaire.setDisponible(true);
            interimaireRepository.save(interimaire);
            mission.setInterimaire(null);
        }

        mission.setStatut("ANNULEE");
        return missionRepository.save(mission);
    }

    @Transactional
    public Mission accepterMission(Long missionId, Long interimaireId) {
        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mission non trouvée"));

        Interimaire interimaire = interimaireRepository.findById(interimaireId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Intérimaire non trouvé"));

        mission.setStatut("ACCEPTEE");
        mission.setInterimaire(interimaire);
        interimaire.setDisponible(false);

        interimaireRepository.save(interimaire);
        return missionRepository.save(mission);
    }
}
