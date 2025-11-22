package com.medical.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "medecin")
@Data
@Getter
@Setter
public class Medecin {
    @Id
    private Integer id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Utilisateur utilisateur;

    @Column(name = "telephone", length = 20)
    @Pattern(regexp = "^[0-9\\s\\-\\+\\(\\)]+$", message = "Le numéro de téléphone n'est pas valide")
    private String telephone;

    @Column(name = "adresse")
    @NotBlank(message = "L'adresse est obligatoire")
    private String adresse;

    @Column(name = "numero_licence", unique = true)
    @NotBlank(message = "Le numéro de licence médicale est obligatoire")
    private String numeroLicence;

    @ManyToOne
    @JoinColumn(name = "specialite_id")
    @NotNull(message = "La spécialité est obligatoire")
    private Specialite specialite;

    @OneToMany(mappedBy = "medecin")
    private List<SecretaireMedecin> secretaireAffectations;

    @OneToMany(mappedBy = "medecin")
    private List<Disponibilite> disponibilites;
}
