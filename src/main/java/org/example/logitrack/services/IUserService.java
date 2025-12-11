package org.example.logitrack.services;

import org.example.logitrack.entities.dto.user.CreateUserDTO;
import org.example.logitrack.entities.dto.user.UpdateUserDTO;
import org.example.logitrack.entities.dto.user.UserDTO;
import org.example.logitrack.entities.enums.Specialite;
import org.example.logitrack.entities.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IUserService {

    Page<UserDTO> getAllUsers(Pageable pageable);

    Page<UserDTO> getTransporteurs(Pageable pageable);

    Page<UserDTO> getTransporteursBySpecialite(Specialite specialite, Pageable pageable);

    UserDTO createTransporteur(CreateUserDTO dto);

    UserDTO updateTransporteur(String id, UpdateUserDTO dto);

    void deleteTransporteur(String id);

    UserDTO activateUser(String id);

    Optional<User> findByLogin(String login);

    Optional<User> findActiveByLogin(String login);
}
