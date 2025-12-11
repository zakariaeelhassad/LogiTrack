package org.example.logitrack.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.logitrack.entities.dto.colis.*;
import org.example.logitrack.entities.enums.ColisStatut;
import org.example.logitrack.entities.enums.ColisType;
import org.example.logitrack.entities.models.User;
import org.example.logitrack.services.IColisService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/colis")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminColisController {

    private final IColisService colisService;

    @GetMapping
    public ResponseEntity<Page<ColisDTO>> getAllColis(
            @RequestParam(required = false) ColisType type,
            @RequestParam(required = false) ColisStatut statut,
            Pageable pageable) {
        return ResponseEntity.ok(colisService.getAllColis(type, statut, pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ColisDTO>> searchByAdresse(
            @RequestParam String adresse,
            Pageable pageable) {
        return ResponseEntity.ok(colisService.searchByAdresse(adresse, pageable));
    }

    @PostMapping
    public ResponseEntity<ColisDTO> createColis(@Valid @RequestBody CreateColisDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(colisService.createColis(dto));
    }

    @PutMapping("/{id}/assign")
    public ResponseEntity<ColisDTO> assignTransporteur(
            @PathVariable String id,
            @Valid @RequestBody AssignTransporteurDTO dto) {
        return ResponseEntity.ok(colisService.assignTransporteur(id, dto.transporteurId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ColisDTO> updateColis(
            @PathVariable String id,
            @Valid @RequestBody UpdateColisDTO dto) {
        return ResponseEntity.ok(colisService.updateColis(id, dto));
    }

    @PatchMapping("/{id}/statut")
    public ResponseEntity<ColisDTO> updateStatut(
            @PathVariable String id,
            @Valid @RequestBody UpdateStatutDTO dto,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(colisService.updateStatut(id, dto.statut(), currentUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteColis(@PathVariable String id) {
        colisService.deleteColis(id);
        return ResponseEntity.noContent().build();
    }
}
