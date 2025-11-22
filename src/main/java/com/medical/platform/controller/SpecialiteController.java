package com.medical.platform.controller;

import com.medical.platform.dto.SpecialiteDTO;
import com.medical.platform.entity.Specialite;
import com.medical.platform.service.SpecialiteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/specialites")
@CrossOrigin(origins = "*", maxAge = 3600)
public class SpecialiteController {

    @Autowired
    private SpecialiteService specialiteService;

    @GetMapping
    public ResponseEntity<List<Specialite>> getAllSpecialites() {
        List<Specialite> specialites = specialiteService.findAll();
        return ResponseEntity.ok(specialites);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSpecialiteById(@PathVariable Integer id) {
        Optional<Specialite> specialite = specialiteService.findById(id);
        return specialite.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/nom/{nom}")
    public ResponseEntity<?> getSpecialiteByNom(@PathVariable String nom) {
        Optional<Specialite> specialite = specialiteService.findByNom(nom);
        return specialite.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/search/{nom}")
    public ResponseEntity<List<Specialite>> searchByNom(@PathVariable String nom) {
        List<Specialite> specialites = specialiteService.searchByNom(nom);
        return ResponseEntity.ok(specialites);
    }

    @PostMapping
    public ResponseEntity<?> createSpecialite(@Valid @RequestBody SpecialiteDTO specialiteDTO) {
        try {
            Specialite specialite = new Specialite();
            specialite.setNom(specialiteDTO.getNom());
            specialite.setDescription(specialiteDTO.getDescription());

            Specialite savedSpecialite = specialiteService.save(specialite);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedSpecialite);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSpecialite(@PathVariable Integer id, @Valid @RequestBody SpecialiteDTO specialiteDTO) {
        Optional<Specialite> specialiteOpt = specialiteService.findById(id);

        if (specialiteOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            Specialite specialite = specialiteOpt.get();
            specialite.setNom(specialiteDTO.getNom());
            specialite.setDescription(specialiteDTO.getDescription());

            Specialite updatedSpecialite = specialiteService.save(specialite);
            return ResponseEntity.ok(updatedSpecialite);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSpecialite(@PathVariable Integer id) {
        try {
            specialiteService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}

