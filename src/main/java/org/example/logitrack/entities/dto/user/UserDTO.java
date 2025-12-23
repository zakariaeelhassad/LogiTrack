package org.example.logitrack.entities.dto.user;

import org.example.logitrack.entities.enums.Role;
import org.example.logitrack.entities.enums.Specialite;
import org.example.logitrack.entities.enums.TransporteurStatut;

public record UserDTO(
        String id,
        String login,
        Role role,
        Boolean active,
        TransporteurStatut statut,
        Specialite specialite
) {
}
