package com.medical.platform.controller;

import com.medical.platform.dto.MedecinDTO;
import com.medical.platform.entity.Medecin;
import com.medical.platform.entity.Utilisateur;
import com.medical.platform.service.MedecinService;
import com.medical.platform.service.SpecialiteService;
import com.medical.platform.service.UtilisateurService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/medecins")
@CrossOrigin(origins = "*", maxAge = 3600)
public class MedecinController {

    @Autowired
    private MedecinService medecinService;

    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private SpecialiteService specialiteService;

    @GetMapping
    public ResponseEntity<List<Medecin>> getAllMedecins() {
        List<Medecin> medecins = medecinService.findAll();
        return ResponseEntity.ok(medecins);
    }

    @GetMapping("/actifs")
    public ResponseEntity<List<Medecin>> getAllMedecinsActifs() {
        List<Medecin> medecins = medecinService.findAllActive();
        return ResponseEntity.ok(medecins);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMedecinById(@PathVariable Integer id) {
        Optional<Medecin> medecin = medecinService.findById(id);
        return medecin.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/search/{nom}")
    public ResponseEntity<List<Medecin>> searchByNom(@PathVariable String nom) {
        List<Medecin> medecins = medecinService.searchByNom(nom);
        return ResponseEntity.ok(medecins);
    }

    @GetMapping("/specialite/{specialiteId}")
    public ResponseEntity<List<Medecin>> getMedecinsBySpecialite(@PathVariable Integer specialiteId) {
        List<Medecin> medecins = medecinService.findBySpecialite(specialiteId);
        return ResponseEntity.ok(medecins);
    }

    @GetMapping("/licence/{numeroLicence}")
    public ResponseEntity<?> getMedecinByLicence(@PathVariable String numeroLicence) {
        Optional<Medecin> medecin = medecinService.findByNumeroLicence(numeroLicence);
        return medecin.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/statistiques/total")
    public ResponseEntity<Long> getTotalMedecins() {
        long total = medecinService.countAllMedecins();
        return ResponseEntity.ok(total);
    }

    @PostMapping
    public ResponseEntity<?> createMedecin(@Valid @RequestBody MedecinDTO medecinDTO) {
        try {
            // Créer l'utilisateur
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setNom(medecinDTO.getNom());
            utilisateur.setPrenom(medecinDTO.getPrenom());
            utilisateur.setEmail(medecinDTO.getEmail());
            utilisateur.setMotDePasse(medecinDTO.getMotDePasse());
            utilisateur.setTypeUtilisateur(Utilisateur.TypeUtilisateur.medecin);
            utilisateur.setRole(Utilisateur.Role.MEDECIN);

            // Créer le médecin
            Medecin medecin = new Medecin();
            medecin.setUtilisateur(utilisateur);
            medecin.setTelephone(medecinDTO.getTelephone());
            medecin.setAdresse(medecinDTO.getAdresse());
            medecin.setNumeroLicence(medecinDTO.getNumeroLicence());
            medecin.setSpecialite(specialiteService.findById(medecinDTO.getSpecialiteId())
                    .orElseThrow(() -> new IllegalArgumentException("Spécialité non trouvée")));

            Medecin savedMedecin = medecinService.save(medecin);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedMedecin);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMedecin(@PathVariable Integer id, @Valid @RequestBody MedecinDTO medecinDTO) {
        Optional<Medecin> medecinOpt = medecinService.findById(id);

        if (medecinOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            Medecin medecin = medecinOpt.get();

            // Mettre à jour les champs
            medecin.getTelephone();
            medecin.setAdresse(medecinDTO.getAdresse());
            if (medecinDTO.getTelephone() != null) {
                medecin.setTelephone(medecinDTO.getTelephone());
            }
            if (medecinDTO.getSpecialiteId() != null) {
                medecin.setSpecialite(specialiteService.findById(medecinDTO.getSpecialiteId())
                        .orElseThrow(() -> new IllegalArgumentException("Spécialité non trouvée")));
            }

            // Mettre à jour l'utilisateur associé
            Utilisateur utilisateur = medecin.getUtilisateur();
            if (medecinDTO.getNom() != null) utilisateur.setNom(medecinDTO.getNom());
            if (medecinDTO.getPrenom() != null) utilisateur.setPrenom(medecinDTO.getPrenom());
            if (medecinDTO.getEmail() != null) utilisateur.setEmail(medecinDTO.getEmail());

            Medecin updatedMedecin = medecinService.save(medecin);
            return ResponseEntity.ok(updatedMedecin);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMedecin(@PathVariable Integer id) {
        try {
            medecinService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/{id}/arreter")
    public ResponseEntity<?> arreterMedecin(@PathVariable Integer id) {
        try {
            medecinService.arreterMedecin(id);
            return ResponseEntity.ok("Médecin désactivé");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}

