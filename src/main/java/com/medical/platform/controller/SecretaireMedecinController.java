package com.medical.platform.controller;

import com.medical.platform.entity.SecretaireMedecin;
import com.medical.platform.service.SecretaireMedecinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/secretaires-medecins")
@CrossOrigin(origins = "*", maxAge = 3600)
public class SecretaireMedecinController {

    @Autowired
    private SecretaireMedecinService secretaireMedecinService;

    @GetMapping
    public ResponseEntity<List<SecretaireMedecin>> getAllAffectations() {
        List<SecretaireMedecin> affectations = secretaireMedecinService.findAll();
        return ResponseEntity.ok(affectations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAffectationById(@PathVariable Integer id) {
        Optional<SecretaireMedecin> affectation = secretaireMedecinService.findById(id);
        return affectation.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/medecin/{medecinId}")
    public ResponseEntity<List<SecretaireMedecin>> getAffectationsByMedecin(@PathVariable Integer medecinId) {
        List<SecretaireMedecin> affectations = secretaireMedecinService.findByMedecin(medecinId);
        return ResponseEntity.ok(affectations);
    }

    @GetMapping("/secretaire/{secretaireId}")
    public ResponseEntity<List<SecretaireMedecin>> getAffectationsBySecretaire(@PathVariable Integer secretaireId) {
        List<SecretaireMedecin> affectations = secretaireMedecinService.findBySecretaire(secretaireId);
        return ResponseEntity.ok(affectations);
    }

    @GetMapping("/medecin/{medecinId}/actifs")
    public ResponseEntity<List<SecretaireMedecin>> getActiveSecretairesForMedecin(@PathVariable Integer medecinId) {
        List<SecretaireMedecin> affectations = secretaireMedecinService.findActiveSecretairesForMedecin(medecinId);
        return ResponseEntity.ok(affectations);
    }

    @PostMapping
    public ResponseEntity<?> createAffectation(@RequestParam Integer medecinId, @RequestParam Integer secretaireId) {
        try {
            SecretaireMedecin affectation = secretaireMedecinService.affecterSecretaire(medecinId, secretaireId);
            return ResponseEntity.status(HttpStatus.CREATED).body(affectation);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAffectation(@PathVariable Integer id) {
        try {
            secretaireMedecinService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/medecin/{medecinId}/secretaire/{secretaireId}")
    public ResponseEntity<?> desaffecterSecretaire(@PathVariable Integer medecinId, @PathVariable Integer secretaireId) {
        try {
            secretaireMedecinService.desaffecterSecretaire(medecinId, secretaireId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}

