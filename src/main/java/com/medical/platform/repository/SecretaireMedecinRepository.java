package com.medical.platform.repository;

import com.medical.platform.entity.SecretaireMedecin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SecretaireMedecinRepository extends JpaRepository<SecretaireMedecin, Integer> {
    List<SecretaireMedecin> findByMedecinId(Integer medecinId);

    List<SecretaireMedecin> findBySecretaireId(Integer secretaireId);

    @Query("SELECT sm FROM SecretaireMedecin sm WHERE sm.medecin.id = ?1 AND sm.secretaire.utilisateur.actif = true")
    List<SecretaireMedecin> findActiveSecretairesForMedecin(Integer medecinId);
}
