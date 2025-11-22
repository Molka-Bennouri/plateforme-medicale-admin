package com.medical.platform.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "specialite ")
@Data
public class Specialite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nom")
    private String nom;
}
