package com.medical.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Entity
@Table(name = "specialite")
@Data
public class Specialite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nom", unique = true)
    @NotBlank(message = "Le nom de la spécialité est obligatoire")
    private String nom;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
}
