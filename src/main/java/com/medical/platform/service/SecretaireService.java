package com.medical.platform.service;

import com.medical.platform.entity.Secretaire;
import com.medical.platform.entity.Utilisateur;
import com.medical.platform.exception.ResourceNotFoundException;
import com.medical.platform.repository.SecretaireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SecretaireService {

    @Autowired
    private SecretaireRepository secretaireRepository;

    @Autowired
    private UtilisateurService utilisateurService;

    public List<Secretaire> findAll() {
        return secretaireRepository.findAll();
    }

    public List<Secretaire> findAllActive() {
        return secretaireRepository.findAllActive();
    }

    public Optional<Secretaire> findById(Integer id) {
        return secretaireRepository.findById(id);
    }

    public List<Secretaire> searchByNom(String nom) {
        return secretaireRepository.findByUtilisateurNomContainingIgnoreCase(nom);
    }

    public long countAllSecretaires() {
        return secretaireRepository.countAllSecretaires();
    }

    @Transactional
    public Secretaire save(Secretaire secretaire) {
        // Sauvegarde de l'utilisateur associé en premier
        Utilisateur utilisateur = secretaire.getUtilisateur();
        if (utilisateur != null) {
            utilisateur.setTypeUtilisateur(Utilisateur.TypeUtilisateur.secretaire);
            utilisateur = utilisateurService.save(utilisateur);
            secretaire.setUtilisateur(utilisateur);
        }

        return secretaireRepository.save(secretaire);
    }

    @Transactional
    public void deleteById(Integer id) {
        if (!secretaireRepository.existsById(id)) {
            throw new ResourceNotFoundException("Secrétaire non trouvé avec l'ID: " + id);
        }
        secretaireRepository.deleteById(id);
    }

    @Transactional
    public Secretaire arreterSecretaire(Integer id) {
        Secretaire secretaire = secretaireRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Secrétaire non trouvé avec l'ID: " + id));

        // Désactiver l'utilisateur associé
        if (secretaire.getUtilisateur() != null) {
            utilisateurService.desactiverUtilisateur(secretaire.getUtilisateur().getId());
        }

        return secretaire;
    }
}

