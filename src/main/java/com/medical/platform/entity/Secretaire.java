package com.medical.platform.entity;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "secretaire ")
public class Secretaire {
    @Id
    private Integer id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Utilisateur utilisateur;

    @Column(name = "bureau", length = 50)
    private String bureau;

    @OneToMany(mappedBy = "secretaire")
    private List<SecretaireMedecin> medecinAffectations;
}
