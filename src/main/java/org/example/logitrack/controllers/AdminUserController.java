package org.example.logitrack.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.logitrack.entities.dto.user.CreateUserDTO;
import org.example.logitrack.entities.dto.user.UpdateUserDTO;
import org.example.logitrack.entities.dto.user.UserDTO;
import org.example.logitrack.entities.enums.Specialite;
import org.example.logitrack.services.IUserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    private final IUserService userService;

    @GetMapping("/users")
    public ResponseEntity<Page<UserDTO>> getAllUsers(Pageable pageable) {
        return ResponseEntity.ok(userService.getAllUsers(pageable));
    }

    @GetMapping("/transporteurs")
    public ResponseEntity<Page<UserDTO>> getTransporteurs(Pageable pageable) {
        return ResponseEntity.ok(userService.getTransporteurs(pageable));
    }

    @GetMapping("/transporteurs/specialite/{specialite}")
    public ResponseEntity<Page<UserDTO>> getTransporteursBySpecialite(
            @PathVariable Specialite specialite,
            Pageable pageable) {
        return ResponseEntity.ok(userService.getTransporteursBySpecialite(specialite, pageable));
    }

    @PostMapping("/transporteurs")
    public ResponseEntity<UserDTO> createTransporteur(@Valid @RequestBody CreateUserDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createTransporteur(dto));
    }

    @PutMapping("/transporteurs/{id}")
    public ResponseEntity<UserDTO> updateTransporteur(
            @PathVariable String id,
            @Valid @RequestBody UpdateUserDTO dto) {
        return ResponseEntity.ok(userService.updateTransporteur(id, dto));
    }

    @DeleteMapping("/transporteurs/{id}")
    public ResponseEntity<Void> deleteTransporteur(@PathVariable String id) {
        userService.deleteTransporteur(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/users/{id}/activate")
    public ResponseEntity<UserDTO> activateUser(@PathVariable String id) {
        return ResponseEntity.ok(userService.activateUser(id));
    }
}