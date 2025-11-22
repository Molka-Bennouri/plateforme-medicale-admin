package com.medical.platform.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class MedecinDTO {
    private Integer id;

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;

    @Email(message = "L'email doit être valide")
    @NotBlank(message = "L'email est obligatoire")
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    private String motDePasse;

    @Pattern(regexp = "^[0-9\\s\\-\\+\\(\\)]+$", message = "Le numéro de téléphone n'est pas valide")
    private String telephone;

    @NotBlank(message = "L'adresse est obligatoire")
    private String adresse;

    @NotBlank(message = "Le numéro de licence médicale est obligatoire")
    private String numeroLicence;

    @NotNull(message = "La spécialité est obligatoire")
    private Integer specialiteId;

    private String specialiteNom;
}

