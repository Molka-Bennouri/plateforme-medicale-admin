package com.medical.platform.repository;

import com.medical.platform.entity.RendezVous;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

    List<RendezVous> findByStatut(RendezVous.StatutRendezVous statut);

    @Query("SELECT r FROM RendezVous r WHERE r.medecin.id = ?1 AND r.dateHeure BETWEEN ?2 AND ?3")
    List<RendezVous> findByMedecinAndDateRange(Integer medecinId, LocalDateTime start, LocalDateTime end);

    @Query("SELECT r FROM RendezVous r WHERE r.patient.id = ?1 AND r.dateHeure >= CURRENT_TIMESTAMP")
    List<RendezVous> findUpcomingForPatient(Integer patientId);

    @Query("SELECT COUNT(r) FROM RendezVous r WHERE r.medecin.id = ?1")
    long countByMedecin(Integer medecinId);
}
