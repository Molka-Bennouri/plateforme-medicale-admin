package com.medical.platform.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "medecin")
@Data
public class Medecin {
    @Id
    private Integer id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Utilisateur utilisateur;

    @Column(name = "telephone", length = 20)
    private String telephone;

    @Column(name = "adresse")
    private String adresse;

    @ManyToOne
    @JoinColumn(name = "specialite_id")
    private Specialite specialite;

    @OneToMany(mappedBy = "medecin")
    private List<SecretaireMedecin> secretaireAffectations;

    @OneToMany(mappedBy = "medecin")
    private List<Disponibilite> disponibilites;
}
