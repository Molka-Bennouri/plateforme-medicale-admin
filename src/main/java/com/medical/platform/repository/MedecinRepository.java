package com.medical.platform.repository;

import com.medical.platform.entity.Medecin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedecinRepository extends JpaRepository<Medecin, Integer> {
    List<Medecin> findBySpecialiteId(Integer specialiteId);

    Optional<Medecin> findByNumeroLicence(String numeroLicence);

    @Query("SELECT m FROM Medecin m WHERE m.utilisateur.actif = true")
    List<Medecin> findAllActive();

    @Query("SELECT COUNT(m) FROM Medecin m")
    long countAllMedecins();

    List<Medecin> findByUtilisateurNomContainingIgnoreCase(String nom);
}
