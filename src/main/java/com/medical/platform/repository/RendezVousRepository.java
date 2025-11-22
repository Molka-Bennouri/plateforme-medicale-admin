package com.medical.platform.repository;

import com.medical.platform.entity.RendezVous;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RendezVousRepository extends JpaRepository<RendezVous, Integer> {
    Page<RendezVous> findAll(Pageable pageable);
    List<RendezVous> findByMedecinId(Integer medecinId);
    List<RendezVous> findByPatientId(Integer patientId);
    List<RendezVous> findByDateHeureBetween(LocalDateTime start, LocalDateTime end);
    long countByStatut(RendezVous.StatutRendezVous statut);
}
