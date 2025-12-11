package org.example.logitrack.entities.dto.colis;

import org.example.logitrack.entities.enums.ColisType;

public record UpdateColisDTO(
        ColisType type,
        Double poids,
        String adresseDestination,
        String instructionsManutention,
        Double temperatureMin,
        Double temperatureMax
) {
}
