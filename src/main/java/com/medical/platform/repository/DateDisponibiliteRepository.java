package com.medical.platform.repository;

import com.medical.platform.entity.DateDisponibilite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface DateDisponibiliteRepository extends JpaRepository<DateDisponibilite, Integer> {
    Optional<DateDisponibilite> findByJour(LocalDate jour);
}

