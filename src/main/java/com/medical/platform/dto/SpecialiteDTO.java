package com.medical.platform.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SpecialiteDTO {
    private Integer id;

    @NotBlank(message = "Le nom de la spécialité est obligatoire")
    private String nom;

    private String description;
}

