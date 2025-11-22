package com.medical.platform.repository;

import com.medical.platform.entity.Secretaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SecretaireRepository extends JpaRepository<Secretaire, Integer> {
    @Query("SELECT s FROM Secretaire s WHERE s.utilisateur.actif = true")
    List<Secretaire> findAllActive();

    @Query("SELECT COUNT(s) FROM Secretaire s")
    long countAllSecretaires();

    List<Secretaire> findByUtilisateurNomContainingIgnoreCase(String nom);
}
