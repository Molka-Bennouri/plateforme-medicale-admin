package com.medical.platform.controller;

import com.medical.platform.dto.RendezVousDTO;
import com.medical.platform.entity.RendezVous;
import com.medical.platform.service.RendezVousService;
import com.medical.platform.service.MedecinService;
import com.medical.platform.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/rendez-vous")
@CrossOrigin(origins = "*", maxAge = 3600)
public class RendezVousController {

    @Autowired
    private RendezVousService rendezVousService;

    @Autowired
    private MedecinService medecinService;

    @Autowired
    private PatientService patientService;

    @GetMapping
    public ResponseEntity<List<RendezVous>> getAllRendezVous() {
        List<RendezVous> rendezVous = rendezVousService.findAll();
        return ResponseEntity.ok(rendezVous);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRendezVousById(@PathVariable Integer id) {
        Optional<RendezVous> rendezVous = rendezVousService.findById(id);
        return rendezVous.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/medecin/{medecinId}")
    public ResponseEntity<List<RendezVous>> getRendezVousByMedecin(@PathVariable Integer medecinId) {
        List<RendezVous> rendezVous = rendezVousService.findByMedecin(medecinId);
        return ResponseEntity.ok(rendezVous);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<RendezVous>> getRendezVousByPatient(@PathVariable Integer patientId) {
        List<RendezVous> rendezVous = rendezVousService.findByPatient(patientId);
        return ResponseEntity.ok(rendezVous);
    }

    @GetMapping("/patient/{patientId}/avenir")
    public ResponseEntity<List<RendezVous>> getUpcomingRendezVousForPatient(@PathVariable Integer patientId) {
        List<RendezVous> rendezVous = rendezVousService.findUpcomingForPatient(patientId);
        return ResponseEntity.ok(rendezVous);
    }

    @PostMapping
    public ResponseEntity<?> createRendezVous(@Valid @RequestBody RendezVousDTO rendezVousDTO) {
        try {
            RendezVous rendezVous = new RendezVous();
            rendezVous.setPatient(patientService.findById(rendezVousDTO.getPatientId())
                    .orElseThrow(() -> new IllegalArgumentException("Patient non trouvé")));
            rendezVous.setMedecin(medecinService.findById(rendezVousDTO.getMedecinId())
                    .orElseThrow(() -> new IllegalArgumentException("Médecin non trouvé")));
            rendezVous.setDateHeure(rendezVousDTO.getDateHeure());
            rendezVous.setMotif(rendezVousDTO.getMotif());

            RendezVous savedRendezVous = rendezVousService.save(rendezVous);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedRendezVous);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRendezVous(@PathVariable Integer id, @Valid @RequestBody RendezVousDTO rendezVousDTO) {
        Optional<RendezVous> rendezVousOpt = rendezVousService.findById(id);

        if (rendezVousOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            RendezVous rendezVous = rendezVousOpt.get();

            if (rendezVousDTO.getDateHeure() != null) {
                rendezVous.setDateHeure(rendezVousDTO.getDateHeure());
            }
            if (rendezVousDTO.getMotif() != null) {
                rendezVous.setMotif(rendezVousDTO.getMotif());
            }

            RendezVous updatedRendezVous = rendezVousService.save(rendezVous);
            return ResponseEntity.ok(updatedRendezVous);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRendezVous(@PathVariable Integer id) {
        try {
            rendezVousService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/{id}/confirmer")
    public ResponseEntity<?> confirmerRendezVous(@PathVariable Integer id) {
        try {
            RendezVous rendezVous = rendezVousService.confirmerRendezVous(id);
            return ResponseEntity.ok(rendezVous);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/{id}/annuler")
    public ResponseEntity<?> annulerRendezVous(@PathVariable Integer id) {
        try {
            RendezVous rendezVous = rendezVousService.annulerRendezVous(id);
            return ResponseEntity.ok(rendezVous);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/{id}/terminer")
    public ResponseEntity<?> terminerRendezVous(@PathVariable Integer id, @RequestParam String ordonnance) {
        try {
            RendezVous rendezVous = rendezVousService.terminerRendezVous(id, ordonnance);
            return ResponseEntity.ok(rendezVous);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}

