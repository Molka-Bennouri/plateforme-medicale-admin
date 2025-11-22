package com.medical.platform.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "patient")
@Data
public class Patient {
    @Id
    private Integer id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Utilisateur utilisateur;

    @Column(name = "date_naissance", nullable = false)
    @NotNull(message = "La date de naissance est obligatoire")
    @Past(message = "La date de naissance doit être dans le passé")
    private LocalDate dateNaissance;

    @Column(name = "telephone", length = 20)
    @Pattern(regexp = "^[0-9\\s\\-\\+\\(\\)]+$", message = "Le numéro de téléphone n'est pas valide")
    private String telephone;

    @Column(name = "numero_dossier", unique = true)
    private String numeroDossier;
}
