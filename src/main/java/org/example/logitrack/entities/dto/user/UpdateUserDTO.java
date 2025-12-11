package org.example.logitrack.entities.dto.user;

import org.example.logitrack.entities.enums.Role;
import org.example.logitrack.entities.enums.Specialite;
import org.example.logitrack.entities.enums.TransporteurStatut;

public record UpdateUserDTO(
        String login,
        String password,
        Boolean active,
        TransporteurStatut statut,
        Specialite specialite
) {
}
