package com.medical.platform.controller;

import com.medical.platform.dto.PatientDTO;
import com.medical.platform.entity.Patient;
import com.medical.platform.entity.Utilisateur;
import com.medical.platform.service.PatientService;
import com.medical.platform.service.UtilisateurService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PatientController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private UtilisateurService utilisateurService;

    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {
        List<Patient> patients = patientService.findAll();
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/actifs")
    public ResponseEntity<List<Patient>> getAllPatientsActifs() {
        List<Patient> patients = patientService.findAllActive();
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPatientById(@PathVariable Integer id) {
        Optional<Patient> patient = patientService.findById(id);
        return patient.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/dossier/{numeroDossier}")
    public ResponseEntity<?> getPatientByDossier(@PathVariable String numeroDossier) {
        Optional<Patient> patient = patientService.findByNumeroDossier(numeroDossier);
        return patient.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/search/{nom}")
    public ResponseEntity<List<Patient>> searchByNom(@PathVariable String nom) {
        List<Patient> patients = patientService.searchByNom(nom);
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/statistiques/total")
    public ResponseEntity<Long> getTotalPatients() {
        long total = patientService.countAllPatients();
        return ResponseEntity.ok(total);
    }

    @PostMapping
    public ResponseEntity<?> createPatient(@Valid @RequestBody PatientDTO patientDTO) {
        try {
            // Créer l'utilisateur
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setNom(patientDTO.getNom());
            utilisateur.setPrenom(patientDTO.getPrenom());
            utilisateur.setEmail(patientDTO.getEmail());
            utilisateur.setMotDePasse(patientDTO.getMotDePasse());
            utilisateur.setTypeUtilisateur(Utilisateur.TypeUtilisateur.patient);
            utilisateur.setRole(Utilisateur.Role.PATIENT);

            // Créer le patient
            Patient patient = new Patient();
            patient.setUtilisateur(utilisateur);
            patient.setDateNaissance(patientDTO.getDateNaissance());
            patient.setTelephone(patientDTO.getTelephone());

            Patient savedPatient = patientService.save(patient);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPatient);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePatient(@PathVariable Integer id, @Valid @RequestBody PatientDTO patientDTO) {
        Optional<Patient> patientOpt = patientService.findById(id);

        if (patientOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            Patient patient = patientOpt.get();

            // Mettre à jour les champs
            if (patientDTO.getTelephone() != null) {
                patient.setTelephone(patientDTO.getTelephone());
            }
            if (patientDTO.getDateNaissance() != null) {
                patient.setDateNaissance(patientDTO.getDateNaissance());
            }

            // Mettre à jour l'utilisateur associé
            Utilisateur utilisateur = patient.getUtilisateur();
            if (patientDTO.getNom() != null) utilisateur.setNom(patientDTO.getNom());
            if (patientDTO.getPrenom() != null) utilisateur.setPrenom(patientDTO.getPrenom());
            if (patientDTO.getEmail() != null) utilisateur.setEmail(patientDTO.getEmail());

            Patient updatedPatient = patientService.save(patient);
            return ResponseEntity.ok(updatedPatient);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePatient(@PathVariable Integer id) {
        try {
            patientService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/{id}/arreter")
    public ResponseEntity<?> arreterPatient(@PathVariable Integer id) {
        try {
            patientService.arreterPatient(id);
            return ResponseEntity.ok("Patient désactivé");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}

