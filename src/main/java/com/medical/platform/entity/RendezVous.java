package com.medical.platform.entity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "rendez_vous")
@Data
public class RendezVous {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "medecin_id", nullable = false)
    private Medecin medecin;

    @Column(name = "date_heure", nullable = false)
    private LocalDateTime dateHeure;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut")
    private StatutRendezVous statut = StatutRendezVous.planifie;

    @Column(name = "est_consultation")
    private Boolean estConsultation = false;

    @Column(name = "ordonnance", columnDefinition = "TEXT")
    private String ordonnance;

    @Column(name = "date_creation")
    private LocalDateTime dateCreation;

    public enum StatutRendezVous {
        planifie, confirme, annule, termine
    }
}
