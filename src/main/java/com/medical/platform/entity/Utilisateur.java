package com.medical.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table (name = "utilisateur")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nom")
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @Column(name = "prenom")
    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;

    @Column(name = "email", unique = true)
    @Email(message = "L'email doit être valide")
    @NotBlank(message = "L'email est obligatoire")
    private String email;

    @Column(name = "mot_de_passe")
    @NotBlank(message = "Le mot de passe est obligatoire")
    private String motDePasse;

    @Column(name = "type_utilisateur")
    @NotNull(message = "Le type d'utilisateur est obligatoire")
    private TypeUtilisateur typeUtilisateur;

    @Column(name = "role")
    private Role role;

    @Column(name = "actif")
    private Boolean actif = true;

    @Column(name = "date_creation")
    private LocalDateTime dateCreation;

    @Column(name = "date_modification")
    private LocalDateTime dateModification;

    public enum TypeUtilisateur {
        medecin, patient, secretaire, admin
    }

    public enum Role {
        ADMIN, MEDECIN, PATIENT, SECRETAIRE
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
