package com.medical.platform.controller;

import com.medical.platform.dto.DisponibiliteDTO;
import com.medical.platform.entity.Disponibilite;
import com.medical.platform.entity.DateDisponibilite;
import com.medical.platform.service.DisponibiliteService;
import com.medical.platform.service.MedecinService;
import com.medical.platform.repository.DateDisponibiliteRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/disponibilites")
@CrossOrigin(origins = "*", maxAge = 3600)
public class DisponibiliteController {

    @Autowired
    private DisponibiliteService disponibiliteService;

    @Autowired
    private MedecinService medecinService;

    @Autowired
    private DateDisponibiliteRepository dateDisponibiliteRepository;

    @GetMapping
    public ResponseEntity<List<Disponibilite>> getAllDisponibilites() {
        List<Disponibilite> disponibilites = disponibiliteService.findAll();
        return ResponseEntity.ok(disponibilites);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDisponibiliteById(@PathVariable Integer id) {
        Optional<Disponibilite> disponibilite = disponibiliteService.findById(id);
        return disponibilite.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/medecin/{medecinId}")
    public ResponseEntity<List<Disponibilite>> getDisponibilitesByMedecin(@PathVariable Integer medecinId) {
        List<Disponibilite> disponibilites = disponibiliteService.findByMedecin(medecinId);
        return ResponseEntity.ok(disponibilites);
    }

    @GetMapping("/medecin/{medecinId}/date/{date}")
    public ResponseEntity<List<Disponibilite>> getDisponibilitesByMedecinAndDate(
            @PathVariable Integer medecinId,
            @PathVariable String date) {
        LocalDate localDate = LocalDate.parse(date);
        List<Disponibilite> disponibilites = disponibiliteService.findByMedecinAndDate(medecinId, localDate);
        return ResponseEntity.ok(disponibilites);
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<Disponibilite>> getDisponibilitesByDate(@PathVariable String date) {
        LocalDate localDate = LocalDate.parse(date);
        List<Disponibilite> disponibilites = disponibiliteService.findByDate(localDate);
        return ResponseEntity.ok(disponibilites);
    }

    @PostMapping
    public ResponseEntity<?> createDisponibilite(@Valid @RequestBody DisponibiliteDTO disponibiliteDTO) {
        try {
            Disponibilite disponibilite = new Disponibilite();
            disponibilite.setMedecin(medecinService.findById(disponibiliteDTO.getMedecinId())
                    .orElseThrow(() -> new IllegalArgumentException("Médecin non trouvé")));

            DateDisponibilite dateDisponibilite = dateDisponibiliteRepository.findById(disponibiliteDTO.getDateId())
                    .orElseThrow(() -> new IllegalArgumentException("Date non trouvée"));
            disponibilite.setDate(dateDisponibilite);

            disponibilite.setHeureDebut(disponibiliteDTO.getHeureDebut());
            disponibilite.setHeureFin(disponibiliteDTO.getHeureFin());

            Disponibilite savedDisponibilite = disponibiliteService.save(disponibilite);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedDisponibilite);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDisponibilite(@PathVariable Integer id, @Valid @RequestBody DisponibiliteDTO disponibiliteDTO) {
        Optional<Disponibilite> disponibiliteOpt = disponibiliteService.findById(id);

        if (disponibiliteOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            Disponibilite disponibilite = disponibiliteOpt.get();

            if (disponibiliteDTO.getHeureDebut() != null) {
                disponibilite.setHeureDebut(disponibiliteDTO.getHeureDebut());
            }
            if (disponibiliteDTO.getHeureFin() != null) {
                disponibilite.setHeureFin(disponibiliteDTO.getHeureFin());
            }

            Disponibilite updatedDisponibilite = disponibiliteService.save(disponibilite);
            return ResponseEntity.ok(updatedDisponibilite);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDisponibilite(@PathVariable Integer id) {
        try {
            disponibiliteService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}

