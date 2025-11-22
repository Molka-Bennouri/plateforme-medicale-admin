package com.medical.platform.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
    @NotNull(message = "Le patient est obligatoire")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "medecin_id", nullable = false)
    @NotNull(message = "Le médecin est obligatoire")
    private Medecin medecin;

    @Column(name = "date_heure", nullable = false)
    @NotNull(message = "La date et l'heure sont obligatoires")
    @Future(message = "La date doit être dans le futur")
    private LocalDateTime dateHeure;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut")
    private StatutRendezVous statut = StatutRendezVous.planifie;

    @Column(name = "est_consultation")
    private Boolean estConsultation = false;

    @Column(name = "ordonnance", columnDefinition = "TEXT")
    private String ordonnance;

    @Column(name = "motif", columnDefinition = "TEXT")
    private String motif;

    @Column(name = "date_creation")
    private LocalDateTime dateCreation;

    @Column(name = "date_modification")
    private LocalDateTime dateModification;

    public enum StatutRendezVous {
        planifie, confirme, annule, termine
    }

    @PrePersist
    protected void onCreate() {
        dateCreation = LocalDateTime.now();
        dateModification = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        dateModification = LocalDateTime.now();
    }
}
