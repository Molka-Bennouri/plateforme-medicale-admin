package com.medical.platform.service;

import com.medical.platform.entity.Disponibilite;
import com.medical.platform.exception.ResourceNotFoundException;
import com.medical.platform.repository.DisponibiliteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DisponibiliteService {

    @Autowired
    private DisponibiliteRepository disponibiliteRepository;

    public List<Disponibilite> findAll() {
        return disponibiliteRepository.findAll();
    }

    public Optional<Disponibilite> findById(Integer id) {
        return disponibiliteRepository.findById(id);
    }

    public List<Disponibilite> findByMedecin(Integer medecinId) {
        return disponibiliteRepository.findByMedecinId(medecinId);
    }

    public List<Disponibilite> findByMedecinAndDate(Integer medecinId, LocalDate date) {
        return disponibiliteRepository.findByMedecinAndDate(medecinId, date);
    }

    public List<Disponibilite> findByDate(LocalDate date) {
        return disponibiliteRepository.findByDateJour(date);
    }

    @Transactional
    public Disponibilite save(Disponibilite disponibilite) {
        // Valider que l'heure de fin est après l'heure de début
        if (disponibilite.getHeureFin().isBefore(disponibilite.getHeureDebut())) {
            throw new IllegalArgumentException("L'heure de fin doit être après l'heure de début");
        }

        return disponibiliteRepository.save(disponibilite);
    }

    @Transactional
    public void deleteById(Integer id) {
        if (!disponibiliteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Disponibilité non trouvée avec l'ID: " + id);
        }
        disponibiliteRepository.deleteById(id);
    }
}

