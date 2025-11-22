package com.medical.platform.service;

import com.medical.platform.entity.Specialite;
import com.medical.platform.exception.DuplicateResourceException;
import com.medical.platform.exception.ResourceNotFoundException;
import com.medical.platform.repository.SpecialiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SpecialiteService {

    @Autowired
    private SpecialiteRepository specialiteRepository;

    public List<Specialite> findAll() {
        return specialiteRepository.findAll();
    }

    public Optional<Specialite> findById(Integer id) {
        return specialiteRepository.findById(id);
    }

    public Optional<Specialite> findByNom(String nom) {
        return specialiteRepository.findByNom(nom);
    }

    public List<Specialite> searchByNom(String nom) {
        return specialiteRepository.findByNomContainingIgnoreCase(nom);
    }

    @Transactional
    public Specialite save(Specialite specialite) {
        // Vérifier que le nom est unique (sauf lors d'une modification)
        if (specialite.getId() == null && specialiteRepository.existsByNom(specialite.getNom())) {
            throw new DuplicateResourceException("Une spécialité avec ce nom existe déjà");
        }

        return specialiteRepository.save(specialite);
    }

    @Transactional
    public void deleteById(Integer id) {
        if (!specialiteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Spécialité non trouvée avec l'ID: " + id);
        }
        specialiteRepository.deleteById(id);
    }
}
