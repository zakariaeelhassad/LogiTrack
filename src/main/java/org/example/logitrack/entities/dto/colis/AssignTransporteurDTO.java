package org.example.logitrack.entities.dto.colis;

import jakarta.validation.constraints.NotBlank;

public record AssignTransporteurDTO(
        @NotBlank(message = "Transporteur ID is required") String transporteurId
) {
}

