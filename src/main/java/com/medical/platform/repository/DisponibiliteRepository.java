package com.medical.platform.repository;

import com.medical.platform.entity.Disponibilite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface DisponibiliteRepository extends JpaRepository<Disponibilite, Integer> {
    List<Disponibilite> findByMedecinId(Integer medecinId);

    @Query("SELECT d FROM Disponibilite d WHERE d.medecin.id = ?1 AND d.date.jour = ?2")
    List<Disponibilite> findByMedecinAndDate(Integer medecinId, LocalDate date);

    @Query("SELECT d FROM Disponibilite d WHERE d.date.jour = ?1")
    List<Disponibilite> findByDateJour(LocalDate date);
}
