package com.medical.platform.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RendezVousDTO {
    private Integer id;

    @NotNull(message = "L'ID du patient est obligatoire")
    private Integer patientId;

    private String patientNom;

    @NotNull(message = "L'ID du médecin est obligatoire")
    private Integer medecinId;

    private String medecinNom;

    @NotNull(message = "La date et l'heure sont obligatoires")
    @Future(message = "La date doit être dans le futur")
    private LocalDateTime dateHeure;

    private String statut;

    private Boolean estConsultation;

    private String ordonnance;

    private String motif;
}

