package com.medical.platform.service;

import com.medical.platform.entity.Medecin;
import com.medical.platform.entity.Utilisateur;
import com.medical.platform.exception.DuplicateResourceException;
import com.medical.platform.exception.ResourceNotFoundException;
import com.medical.platform.repository.MedecinRepository;
import com.medical.platform.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MedecinService {

    @Autowired
    private MedecinRepository medecinRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private UtilisateurService utilisateurService;

    public List<Medecin> findAll() {
        return medecinRepository.findAll();
    }

    public List<Medecin> findAllActive() {
        return medecinRepository.findAllActive();
    }

    public Optional<Medecin> findById(Integer id) {
        return medecinRepository.findById(id);
    }

    public List<Medecin> findBySpecialite(Integer specialiteId) {
        return medecinRepository.findBySpecialiteId(specialiteId);
    }

    public Optional<Medecin> findByNumeroLicence(String numeroLicence) {
        return medecinRepository.findByNumeroLicence(numeroLicence);
    }

    public List<Medecin> searchByNom(String nom) {
        return medecinRepository.findByUtilisateurNomContainingIgnoreCase(nom);
    }

    public long countAllMedecins() {
        return medecinRepository.countAllMedecins();
    }

    @Transactional
    public Medecin save(Medecin medecin) {
        // Vérifier que le numéro de licence est unique
        if (medecin.getId() == null && medecinRepository.findByNumeroLicence(medecin.getNumeroLicence()).isPresent()) {
            throw new DuplicateResourceException("Un médecin avec ce numéro de licence existe déjà");
        }

        // Sauvegarde de l'utilisateur associé en premier
        Utilisateur utilisateur = medecin.getUtilisateur();
        if (utilisateur != null) {
            utilisateur.setTypeUtilisateur(Utilisateur.TypeUtilisateur.medecin);
            utilisateur = utilisateurService.save(utilisateur);
            medecin.setUtilisateur(utilisateur);
        }

        return medecinRepository.save(medecin);
    }

    @Transactional
    public void deleteById(Integer id) {
        if (!medecinRepository.existsById(id)) {
            throw new ResourceNotFoundException("Médecin non trouvé avec l'ID: " + id);
        }
        medecinRepository.deleteById(id);
    }

    @Transactional
    public Medecin arreterMedecin(Integer id) {
        Medecin medecin = medecinRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Médecin non trouvé avec l'ID: " + id));

        // Désactiver l'utilisateur associé
        if (medecin.getUtilisateur() != null) {
            utilisateurService.desactiverUtilisateur(medecin.getUtilisateur().getId());
        }

        return medecin;
    }
}
