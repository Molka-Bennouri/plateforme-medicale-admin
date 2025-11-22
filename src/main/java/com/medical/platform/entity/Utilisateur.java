package com.medical.platform.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table (name = "utilisateur")
@Data
public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom")
    private String prenom;

    @Column(name = "mot_de_passe")
    private String motDePasse;

    @Column(name = "type_utilisateur")
    private TypeUtilisateur typeUtilisateur;

    public enum TypeUtilisateur {
        medecin, patient, secretaire
    }
}
