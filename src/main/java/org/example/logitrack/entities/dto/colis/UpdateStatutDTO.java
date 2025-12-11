package org.example.logitrack.entities.dto.colis;

import jakarta.validation.constraints.NotNull;
import org.example.logitrack.entities.enums.ColisStatut;

public record UpdateStatutDTO(
        @NotNull(message = "Statut is required") ColisStatut statut
) {
}
