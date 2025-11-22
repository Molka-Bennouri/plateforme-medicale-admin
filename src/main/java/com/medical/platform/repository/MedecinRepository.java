package com.medical.platform.repository;

import com.medical.platform.entity.Medecin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedecinRepository extends JpaRepository<Medecin, Integer> {
    List<Medecin> findAll();  // List au lieu de Page
    List<Medecin> findBySpecialiteId(Integer specialiteId);
    long countBy(); // Nombre total de médecins
}
