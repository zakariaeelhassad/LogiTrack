package org.example.logitrack.entities.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.example.logitrack.entities.enums.Role;
import org.example.logitrack.entities.enums.Specialite;
import org.example.logitrack.entities.enums.TransporteurStatut;

public record CreateUserDTO(
        @NotBlank String login,
        @NotBlank String password,
        @NotNull Role role,
        @NotNull TransporteurStatut statut,
        @NotNull Specialite specialite
) {
}
