package org.example.logitrack.entities.dto.colis;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.example.logitrack.entities.enums.ColisStatut;
import org.example.logitrack.entities.enums.ColisType;

public record ColisDTO(
        String id,
        @NotNull(message = "Type is required") ColisType type,
        @NotNull(message = "Poids is required") @Positive(message = "Poids must be positive") Double poids,
        @NotBlank(message = "Adresse destination is required") String adresseDestination,
        ColisStatut statut,
        String transporteurId,
        String transporteurLogin,
        String instructionsManutention,
        Double temperatureMin,
        Double temperatureMax
) {
}
