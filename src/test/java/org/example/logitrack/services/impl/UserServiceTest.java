package org.example.logitrack.services.impl;

import org.example.logitrack.entities.dto.user.CreateUserDTO;
import org.example.logitrack.entities.dto.user.UpdateUserDTO;
import org.example.logitrack.entities.dto.user.UserDTO;
import org.example.logitrack.entities.enums.Role;
import org.example.logitrack.entities.enums.Specialite;
import org.example.logitrack.entities.enums.TransporteurStatut;
import org.example.logitrack.entities.models.User;
import org.example.logitrack.mappers.UserMapper;
import org.example.logitrack.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId("1");
        user.setLogin("transport1");
        user.setRole(Role.TRANSPORTEUR);
        user.setActive(true);
        user.setStatut(TransporteurStatut.DISPONIBLE);
        user.setSpecialite(Specialite.STANDARD);

        userDTO = new UserDTO(
                "1",
                "transport1",
                Role.TRANSPORTEUR,
                true,
                TransporteurStatut.DISPONIBLE,
                Specialite.STANDARD
        );

    }

    @Test
    void getAllUsers_success() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<User> page = new PageImpl<>(List.of(user));

        when(userRepository.findAll(pageable)).thenReturn(page);
        when(userMapper.toDto(user)).thenReturn(userDTO);

        Page<UserDTO> result = userService.getAllUsers(pageable);

        assertEquals(1, result.getTotalElements());
        verify(userRepository).findAll(pageable);
    }

    @Test
    void getTransporteurs_success() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<User> page = new PageImpl<>(List.of(user));

        when(userRepository.findByRole(Role.TRANSPORTEUR, pageable)).thenReturn(page);
        when(userMapper.toDto(user)).thenReturn(userDTO);

        Page<UserDTO> result = userService.getTransporteurs(pageable);

        assertEquals(1, result.getContent().size());
        verify(userRepository).findByRole(Role.TRANSPORTEUR, pageable);
    }

    @Test
    void createTransporteur_success() {
        CreateUserDTO dto = new CreateUserDTO(
                "transport1",
                "1234",
                Role.TRANSPORTEUR,
                TransporteurStatut.DISPONIBLE,
                Specialite.STANDARD
        );

        when(userRepository.findByLogin(dto.login())).thenReturn(Optional.empty());
        when(userMapper.toEntity(dto)).thenReturn(user);
        when(passwordEncoder.encode("1234")).thenReturn("hashed");
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDTO);

        UserDTO result = userService.createTransporteur(dto);

        assertNotNull(result);
        verify(userRepository).save(user);
    }

    @Test
    void createTransporteur_loginAlreadyExists() {
        CreateUserDTO dto = mock(CreateUserDTO.class);

        when(userRepository.findByLogin(any())).thenReturn(Optional.of(user));

        assertThrows(IllegalArgumentException.class,
                () -> userService.createTransporteur(dto));
    }

    @Test
    void updateTransporteur_success() {
        UpdateUserDTO dto = new UpdateUserDTO(
                "newLogin",
                "newPwd",
                false,
                TransporteurStatut.DISPONIBLE,
                Specialite.STANDARD
        );

        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("newPwd")).thenReturn("hashed");
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDTO);

        UserDTO result = userService.updateTransporteur("1", dto);

        assertNotNull(result);
        verify(userRepository).save(user);
    }

    @Test
    void deleteTransporteur_success() {
        when(userRepository.findById("1")).thenReturn(Optional.of(user));

        userService.deleteTransporteur("1");

        verify(userRepository).delete(user);
    }

    @Test
    void findByLogin_success() {
        when(userRepository.findByLogin("transport1")).thenReturn(Optional.of(user));

        Optional<User> result = userService.findByLogin("transport1");

        assertTrue(result.isPresent());
    }

    @Test
    void findActiveByLogin_success() {
        when(userRepository.findByLoginAndActiveTrue("transport1"))
                .thenReturn(Optional.of(user));

        Optional<User> result = userService.findActiveByLogin("transport1");

        assertTrue(result.isPresent());
    }
}
