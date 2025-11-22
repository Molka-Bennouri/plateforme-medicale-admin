package com.medical.platform.repository;

import com.medical.platform.entity.Specialite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SpecialiteRepository extends JpaRepository<Specialite, Integer> {
    Optional<Specialite> findByNom(String nom);

    List<Specialite> findByNomContainingIgnoreCase(String nom);

    boolean existsByNom(String nom);
}
