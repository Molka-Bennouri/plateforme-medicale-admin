package com.medical.platform.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class DisponibiliteDTO {
    private Integer id;

    @NotNull(message = "L'ID du médecin est obligatoire")
    private Integer medecinId;

    private String medecinNom;

    @NotNull(message = "La date est obligatoire")
    private LocalDate date;

    @NotNull(message = "L'heure de début est obligatoire")
    private LocalTime heureDebut;

    @NotNull(message = "L'heure de fin est obligatoire")
    private LocalTime heureFin;
}

