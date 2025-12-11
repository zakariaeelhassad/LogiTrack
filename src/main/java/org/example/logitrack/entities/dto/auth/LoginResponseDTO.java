package org.example.logitrack.entities.dto.auth;

import org.example.logitrack.entities.dto.user.UserDTO;

public record LoginResponseDTO(
        String token,
        UserDTO user
) {
}
