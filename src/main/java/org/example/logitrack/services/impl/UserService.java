package org.example.logitrack.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.logitrack.entities.dto.user.CreateUserDTO;
import org.example.logitrack.entities.dto.user.UpdateUserDTO;
import org.example.logitrack.entities.dto.user.UserDTO;
import org.example.logitrack.entities.enums.Role;
import org.example.logitrack.entities.enums.Specialite;
import org.example.logitrack.entities.enums.TransporteurStatut;
import org.example.logitrack.entities.models.User;
import org.example.logitrack.mappers.UserMapper;
import org.example.logitrack.repositories.UserRepository;
import org.example.logitrack.services.IUserService;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public Page<UserDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::toDto);
    }

    public Page<UserDTO> getTransporteurs(Pageable pageable) {
        return userRepository.findByRole(Role.TRANSPORTEUR, pageable)
                .map(userMapper::toDto);
    }

    public Page<UserDTO> getTransporteursBySpecialite(Specialite specialite, Pageable pageable) {
        return userRepository.findByRoleAndSpecialite(Role.TRANSPORTEUR, specialite, pageable)
                .map(userMapper::toDto);
    }

    public UserDTO createTransporteur(CreateUserDTO dto) {
        if (userRepository.findByLogin(dto.login()).isPresent()) {
            throw new IllegalArgumentException("User with this login already exists");
        }

        User user = userMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setRole(Role.TRANSPORTEUR);

        if (dto.statut() == null) {
            user.setStatut(TransporteurStatut.DISPONIBLE);
        }

        User saved = userRepository.save(user);
        return userMapper.toDto(saved);
    }

    public UserDTO updateTransporteur(String id, UpdateUserDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (user.getRole() != Role.TRANSPORTEUR) {
            throw new IllegalArgumentException("User is not a transporteur");
        }

        Optional.ofNullable(dto.login()).ifPresent(user::setLogin);
        Optional.ofNullable(dto.password()).ifPresent(pwd ->
                user.setPassword(passwordEncoder.encode(pwd)));
        Optional.ofNullable(dto.active()).ifPresent(user::setActive);
        Optional.ofNullable(dto.statut()).ifPresent(user::setStatut);
        Optional.ofNullable(dto.specialite()).ifPresent(user::setSpecialite);

        User updated = userRepository.save(user);
        return userMapper.toDto(updated);
    }

    public void deleteTransporteur(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (user.getRole() != Role.TRANSPORTEUR) {
            throw new IllegalArgumentException("User is not a transporteur");
        }

        userRepository.delete(user);
    }

    public UserDTO activateUser(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setActive(true);
        User updated = userRepository.save(user);
        return userMapper.toDto(updated);
    }

    public Optional<User> findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    public Optional<User> findActiveByLogin(String login) {
        return userRepository.findByLoginAndActiveTrue(login);
    }
}
