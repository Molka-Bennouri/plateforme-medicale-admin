package com.medical.platform.entity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "secretaire_medecin")
@Data
public class SecretaireMedecin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "secretaire_id", nullable = false)
    private Secretaire secretaire;

    @ManyToOne
    @JoinColumn(name = "medecin_id", nullable = false)
    private Medecin medecin;

    @Column(name = "date_affectation")
    private LocalDateTime dateAffectation;
}
