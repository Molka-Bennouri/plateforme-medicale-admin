package com.medical.platform.repository;

import com.medical.platform.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Integer> {
    Optional<Utilisateur> findByNom(String nom);

    Optional<Utilisateur> findByEmail(String email);

    List<Utilisateur> findByTypeUtilisateur(Utilisateur.TypeUtilisateur typeUtilisateur);

    List<Utilisateur> findByActifTrue();

    List<Utilisateur> findByNomContainingIgnoreCase(String nom);

    boolean existsByEmail(String email);
}
