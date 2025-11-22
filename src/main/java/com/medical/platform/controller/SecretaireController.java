package com.medical.platform.controller;

import com.medical.platform.dto.SecretaireDTO;
import com.medical.platform.entity.Secretaire;
import com.medical.platform.entity.Utilisateur;
import com.medical.platform.service.SecretaireService;
import com.medical.platform.service.UtilisateurService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/secretaires")
@CrossOrigin(origins = "*", maxAge = 3600)
public class SecretaireController {

    @Autowired
    private SecretaireService secretaireService;

    @Autowired
    private UtilisateurService utilisateurService;

    @GetMapping
    public ResponseEntity<List<Secretaire>> getAllSecretaires() {
        List<Secretaire> secretaires = secretaireService.findAll();
        return ResponseEntity.ok(secretaires);
    }

    @GetMapping("/actifs")
    public ResponseEntity<List<Secretaire>> getAllSecretairesActifs() {
        List<Secretaire> secretaires = secretaireService.findAllActive();
        return ResponseEntity.ok(secretaires);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSecretaireById(@PathVariable Integer id) {
        Optional<Secretaire> secretaire = secretaireService.findById(id);
        return secretaire.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/search/{nom}")
    public ResponseEntity<List<Secretaire>> searchByNom(@PathVariable String nom) {
        List<Secretaire> secretaires = secretaireService.searchByNom(nom);
        return ResponseEntity.ok(secretaires);
    }

    @GetMapping("/statistiques/total")
    public ResponseEntity<Long> getTotalSecretaires() {
        long total = secretaireService.countAllSecretaires();
        return ResponseEntity.ok(total);
    }

    @PostMapping
    public ResponseEntity<?> createSecretaire(@Valid @RequestBody SecretaireDTO secretaireDTO) {
        try {
            // Créer l'utilisateur
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setNom(secretaireDTO.getNom());
            utilisateur.setPrenom(secretaireDTO.getPrenom());
            utilisateur.setEmail(secretaireDTO.getEmail());
            utilisateur.setMotDePasse(secretaireDTO.getMotDePasse());
            utilisateur.setTypeUtilisateur(Utilisateur.TypeUtilisateur.secretaire);
            utilisateur.setRole(Utilisateur.Role.SECRETAIRE);

            // Créer la secrétaire
            Secretaire secretaire = new Secretaire();
            secretaire.setUtilisateur(utilisateur);
            secretaire.setBureau(secretaireDTO.getBureau());

            Secretaire savedSecretaire = secretaireService.save(secretaire);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedSecretaire);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSecretaire(@PathVariable Integer id, @Valid @RequestBody SecretaireDTO secretaireDTO) {
        Optional<Secretaire> secretaireOpt = secretaireService.findById(id);

        if (secretaireOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            Secretaire secretaire = secretaireOpt.get();

            // Mettre à jour les champs
            if (secretaireDTO.getBureau() != null) {
                secretaire.setBureau(secretaireDTO.getBureau());
            }

            // Mettre à jour l'utilisateur associé
            Utilisateur utilisateur = secretaire.getUtilisateur();
            if (secretaireDTO.getNom() != null) utilisateur.setNom(secretaireDTO.getNom());
            if (secretaireDTO.getPrenom() != null) utilisateur.setPrenom(secretaireDTO.getPrenom());
            if (secretaireDTO.getEmail() != null) utilisateur.setEmail(secretaireDTO.getEmail());

            Secretaire updatedSecretaire = secretaireService.save(secretaire);
            return ResponseEntity.ok(updatedSecretaire);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSecretaire(@PathVariable Integer id) {
        try {
            secretaireService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/{id}/arreter")
    public ResponseEntity<?> arreterSecretaire(@PathVariable Integer id) {
        try {
            secretaireService.arreterSecretaire(id);
            return ResponseEntity.ok("Secrétaire désactivée");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}

