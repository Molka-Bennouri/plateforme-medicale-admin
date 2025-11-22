package com.medical.platform.service;

import com.medical.platform.entity.RendezVous;
import com.medical.platform.exception.ResourceNotFoundException;
import com.medical.platform.repository.RendezVousRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RendezVousService {

    @Autowired
    private RendezVousRepository rendezVousRepository;

    public List<RendezVous> findAll() {
        return rendezVousRepository.findAll();
    }

    public Optional<RendezVous> findById(Integer id) {
        return rendezVousRepository.findById(id);
    }

    public List<RendezVous> findByMedecin(Integer medecinId) {
        return rendezVousRepository.findByMedecinId(medecinId);
    }

    public List<RendezVous> findByPatient(Integer patientId) {
        return rendezVousRepository.findByPatientId(patientId);
    }

    public List<RendezVous> findUpcomingForPatient(Integer patientId) {
        return rendezVousRepository.findUpcomingForPatient(patientId);
    }

    public List<RendezVous> findByDateRange(LocalDateTime start, LocalDateTime end) {
        return rendezVousRepository.findByDateHeureBetween(start, end);
    }

    public List<RendezVous> findByMedecinAndDateRange(Integer medecinId, LocalDateTime start, LocalDateTime end) {
        return rendezVousRepository.findByMedecinAndDateRange(medecinId, start, end);
    }

    public List<RendezVous> findByStatut(RendezVous.StatutRendezVous statut) {
        return rendezVousRepository.findByStatut(statut);
    }

    public long countByStatut(RendezVous.StatutRendezVous statut) {
        return rendezVousRepository.countByStatut(statut);
    }

    public long countByMedecin(Integer medecinId) {
        return rendezVousRepository.countByMedecin(medecinId);
    }

    @Transactional
    public RendezVous save(RendezVous rendezVous) {
        // Valider que la date est dans le futur
        if (rendezVous.getDateHeure().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La date du rendez-vous doit être dans le futur");
        }

        return rendezVousRepository.save(rendezVous);
    }

    @Transactional
    public void deleteById(Integer id) {
        if (!rendezVousRepository.existsById(id)) {
            throw new ResourceNotFoundException("Rendez-vous non trouvé avec l'ID: " + id);
        }
        rendezVousRepository.deleteById(id);
    }

    @Transactional
    public RendezVous confirmerRendezVous(Integer id) {
        RendezVous rendezVous = rendezVousRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rendez-vous non trouvé avec l'ID: " + id));

        if (rendezVous.getStatut() != RendezVous.StatutRendezVous.planifie) {
            throw new IllegalStateException("Seuls les rendez-vous planifiés peuvent être confirmés");
        }

        rendezVous.setStatut(RendezVous.StatutRendezVous.confirme);
        return rendezVousRepository.save(rendezVous);
    }

    @Transactional
    public RendezVous annulerRendezVous(Integer id) {
        RendezVous rendezVous = rendezVousRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rendez-vous non trouvé avec l'ID: " + id));

        if (rendezVous.getStatut() == RendezVous.StatutRendezVous.termine ||
            rendezVous.getStatut() == RendezVous.StatutRendezVous.annule) {
            throw new IllegalStateException("Ce rendez-vous ne peut pas être annulé");
        }

        rendezVous.setStatut(RendezVous.StatutRendezVous.annule);
        return rendezVousRepository.save(rendezVous);
    }

    @Transactional
    public RendezVous terminerRendezVous(Integer id, String ordonnance) {
        RendezVous rendezVous = rendezVousRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rendez-vous non trouvé avec l'ID: " + id));

        rendezVous.setStatut(RendezVous.StatutRendezVous.termine);
        rendezVous.setEstConsultation(true);
        rendezVous.setOrdonnance(ordonnance);
        return rendezVousRepository.save(rendezVous);
    }
}
