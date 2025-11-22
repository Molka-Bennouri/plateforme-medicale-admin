package com.medical.platform.controller;

import com.medical.platform.dto.UtilisateurDTO;
import com.medical.platform.entity.Utilisateur;
import com.medical.platform.service.UtilisateurService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/utilisateurs")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UtilisateurController {

    @Autowired
    private UtilisateurService utilisateurService;

    @GetMapping
    public ResponseEntity<List<Utilisateur>> getAllUtilisateurs() {
        List<Utilisateur> utilisateurs = utilisateurService.findAll();
        return ResponseEntity.ok(utilisateurs);
    }

    @GetMapping("/actifs")
    public ResponseEntity<List<Utilisateur>> getAllUtilisateursActifs() {
        List<Utilisateur> utilisateurs = utilisateurService.findAllActive();
        return ResponseEntity.ok(utilisateurs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUtilisateurById(@PathVariable Integer id) {
        Optional<Utilisateur> utilisateur = utilisateurService.findById(id);
        return utilisateur.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<?> getUtilisateurByEmail(@PathVariable String email) {
        Optional<Utilisateur> utilisateur = utilisateurService.findByEmail(email);
        return utilisateur.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/type/{typeUtilisateur}")
    public ResponseEntity<List<Utilisateur>> getUtilisateursByType(@PathVariable String typeUtilisateur) {
        try {
            Utilisateur.TypeUtilisateur type = Utilisateur.TypeUtilisateur.valueOf(typeUtilisateur);
            List<Utilisateur> utilisateurs = utilisateurService.findByTypeUtilisateur(type);
            return ResponseEntity.ok(utilisateurs);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/search/{nom}")
    public ResponseEntity<List<Utilisateur>> searchByNom(@PathVariable String nom) {
        List<Utilisateur> utilisateurs = utilisateurService.searchByNom(nom);
        return ResponseEntity.ok(utilisateurs);
    }

    @PostMapping
    public ResponseEntity<?> createUtilisateur(@Valid @RequestBody UtilisateurDTO utilisateurDTO) {
        try {
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setNom(utilisateurDTO.getNom());
            utilisateur.setPrenom(utilisateurDTO.getPrenom());
            utilisateur.setEmail(utilisateurDTO.getEmail());
            utilisateur.setMotDePasse(utilisateurDTO.getMotDePasse());
            utilisateur.setTypeUtilisateur(Utilisateur.TypeUtilisateur.valueOf(utilisateurDTO.getTypeUtilisateur()));

            Utilisateur savedUtilisateur = utilisateurService.save(utilisateur);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUtilisateur);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUtilisateur(@PathVariable Integer id, @Valid @RequestBody UtilisateurDTO utilisateurDTO) {
        Optional<Utilisateur> utilisateurOpt = utilisateurService.findById(id);

        if (utilisateurOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            Utilisateur utilisateur = utilisateurOpt.get();

            if (utilisateurDTO.getNom() != null) utilisateur.setNom(utilisateurDTO.getNom());
            if (utilisateurDTO.getPrenom() != null) utilisateur.setPrenom(utilisateurDTO.getPrenom());
            if (utilisateurDTO.getEmail() != null) utilisateur.setEmail(utilisateurDTO.getEmail());
            if (utilisateurDTO.getMotDePasse() != null) utilisateur.setMotDePasse(utilisateurDTO.getMotDePasse());

            Utilisateur updatedUtilisateur = utilisateurService.save(utilisateur);
            return ResponseEntity.ok(updatedUtilisateur);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUtilisateur(@PathVariable Integer id) {
        try {
            utilisateurService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/{id}/activer")
    public ResponseEntity<?> activerUtilisateur(@PathVariable Integer id) {
        try {
            Utilisateur utilisateur = utilisateurService.activerUtilisateur(id);
            return ResponseEntity.ok(utilisateur);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/{id}/desactiver")
    public ResponseEntity<?> desactiverUtilisateur(@PathVariable Integer id) {
        try {
            Utilisateur utilisateur = utilisateurService.desactiverUtilisateur(id);
            return ResponseEntity.ok(utilisateur);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}

