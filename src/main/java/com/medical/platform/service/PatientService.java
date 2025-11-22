package com.medical.platform.service;

import com.medical.platform.entity.Patient;
import com.medical.platform.entity.Utilisateur;
import com.medical.platform.exception.ResourceNotFoundException;
import com.medical.platform.repository.PatientRepository;
import com.medical.platform.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private UtilisateurService utilisateurService;

    public List<Patient> findAll() {
        return patientRepository.findAll();
    }

    public List<Patient> findAllActive() {
        return patientRepository.findAllActive();
    }

    public Optional<Patient> findById(Integer id) {
        return patientRepository.findById(id);
    }

    public Optional<Patient> findByNumeroDossier(String numeroDossier) {
        return patientRepository.findByNumeroDossier(numeroDossier);
    }

    public List<Patient> searchByNom(String nom) {
        return patientRepository.findByUtilisateurNomContainingIgnoreCase(nom);
    }

    public long countAllPatients() {
        return patientRepository.countAllPatients();
    }

    @Transactional
    public Patient save(Patient patient) {
        // Générer un numéro de dossier s'il n'existe pas
        if (patient.getNumeroDossier() == null || patient.getNumeroDossier().isEmpty()) {
            patient.setNumeroDossier("PAT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }

        // Sauvegarde de l'utilisateur associé en premier
        Utilisateur utilisateur = patient.getUtilisateur();
        if (utilisateur != null) {
            utilisateur.setTypeUtilisateur(Utilisateur.TypeUtilisateur.patient);
            utilisateur = utilisateurService.save(utilisateur);
            patient.setUtilisateur(utilisateur);
        }

        return patientRepository.save(patient);
    }

    @Transactional
    public void deleteById(Integer id) {
        if (!patientRepository.existsById(id)) {
            throw new ResourceNotFoundException("Patient non trouvé avec l'ID: " + id);
        }
        patientRepository.deleteById(id);
    }

    @Transactional
    public Patient arreterPatient(Integer id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient non trouvé avec l'ID: " + id));

        // Désactiver l'utilisateur associé
        if (patient.getUtilisateur() != null) {
            utilisateurService.desactiverUtilisateur(patient.getUtilisateur().getId());
        }

        return patient;
    }
}
