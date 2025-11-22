package com.medical.platform.repository;

import com.medical.platform.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {
    Optional<Patient> findByNumeroDossier(String numeroDossier);

    @Query("SELECT COUNT(p) FROM Patient p")
    long countAllPatients();

    @Query("SELECT p FROM Patient p WHERE p.utilisateur.actif = true")
    List<Patient> findAllActive();

    List<Patient> findByUtilisateurNomContainingIgnoreCase(String nom);
}
