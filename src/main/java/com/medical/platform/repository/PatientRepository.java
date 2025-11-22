package com.medical.platform.repository;

import com.medical.platform.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {
    long countBy(); // Nombre total de patients
}
