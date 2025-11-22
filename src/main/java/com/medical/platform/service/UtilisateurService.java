package com.medical.platform.service;

import com.medical.platform.entity.Utilisateur;
import com.medical.platform.exception.DuplicateResourceException;
import com.medical.platform.exception.ResourceNotFoundException;
import com.medical.platform.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UtilisateurService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Utilisateur> findAll() {
        return utilisateurRepository.findAll();
    }

    public Optional<Utilisateur> findById(Integer id) {
        return utilisateurRepository.findById(id);
    }

    public Optional<Utilisateur> findByEmail(String email) {
        return utilisateurRepository.findByEmail(email);
    }

    public List<Utilisateur> findByTypeUtilisateur(Utilisateur.TypeUtilisateur typeUtilisateur) {
        return utilisateurRepository.findByTypeUtilisateur(typeUtilisateur);
    }

    public List<Utilisateur> findAllActive() {
        return utilisateurRepository.findByActifTrue();
    }

    public List<Utilisateur> searchByNom(String nom) {
        return utilisateurRepository.findByNomContainingIgnoreCase(nom);
    }

    @Transactional
    public Utilisateur save(Utilisateur utilisateur) {
        // Vérifier si l'email existe déjà (lors de création)
        if (utilisateur.getId() == null && utilisateurRepository.existsByEmail(utilisateur.getEmail())) {
            throw new DuplicateResourceException("Un utilisateur avec cet email existe déjà");
        }

        // Hacher le mot de passe si c'est une nouvelle création ou modification
        if (utilisateur.getId() == null) {
            utilisateur.setMotDePasse(passwordEncoder.encode(utilisateur.getMotDePasse()));
        } else {
            // Vérifier si le mot de passe a changé
            Optional<Utilisateur> existing = utilisateurRepository.findById(utilisateur.getId());
            if (existing.isPresent() && !existing.get().getMotDePasse().equals(utilisateur.getMotDePasse())) {
                utilisateur.setMotDePasse(passwordEncoder.encode(utilisateur.getMotDePasse()));
            }
        }

        return utilisateurRepository.save(utilisateur);
    }

    @Transactional
    public void deleteById(Integer id) {
        if (!utilisateurRepository.existsById(id)) {
            throw new ResourceNotFoundException("Utilisateur non trouvé avec l'ID: " + id);
        }
        utilisateurRepository.deleteById(id);
    }

    @Transactional
    public Utilisateur activerUtilisateur(Integer id) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'ID: " + id));
        utilisateur.setActif(true);
        return utilisateurRepository.save(utilisateur);
    }

    @Transactional
    public Utilisateur desactiverUtilisateur(Integer id) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'ID: " + id));
        utilisateur.setActif(false);
        return utilisateurRepository.save(utilisateur);
    }

    public boolean validatePassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
