package com.medical.platform.controller;

import com.medical.platform.service.StatistiqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/statistiques")
@CrossOrigin(origins = "*", maxAge = 3600)
public class StatistiqueController {

    @Autowired
    private StatistiqueService statistiqueService;

    @GetMapping("/general")
    public ResponseEntity<Map<String, Object>> getGeneralStatistics() {
        Map<String, Object> stats = statistiqueService.getGeneralStatistics();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/medecins")
    public ResponseEntity<List<Map<String, Object>>> getStatistiquesMedecins() {
        List<Map<String, Object>> stats = statistiqueService.getStatistiquesMedecins();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/medecin/{medecinId}/evaluation")
    public ResponseEntity<Map<String, Object>> evaluateMedecin(@PathVariable Integer medecinId) {
        Map<String, Object> evaluation = statistiqueService.evaluateMedecin(medecinId);
        return ResponseEntity.ok(evaluation);
    }

    @GetMapping("/secretaire/{secretaireId}/evaluation")
    public ResponseEntity<Map<String, Object>> evaluateSecretaire(@PathVariable Integer secretaireId) {
        Map<String, Object> evaluation = statistiqueService.evaluateSecretaire(secretaireId);
        return ResponseEntity.ok(evaluation);
    }

    @GetMapping("/rendez-vous/avenir")
    public ResponseEntity<List<Map<String, Object>>> getRendezVousAVenir() {
        List<Map<String, Object>> rendezVous = statistiqueService.getRendezVousAVenir();
        return ResponseEntity.ok(rendezVous);
    }

    @GetMapping("/rendez-vous/par-specialite")
    public ResponseEntity<Map<String, Long>> getRendezVousParSpecialite() {
        Map<String, Long> stats = statistiqueService.getRendezVousParSpecialite();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/confirmation/taux")
    public ResponseEntity<Map<String, Object>> getTauxConfirmation() {
        Map<String, Object> stats = statistiqueService.getTauxConfirmation();
        return ResponseEntity.ok(stats);
    }
}

