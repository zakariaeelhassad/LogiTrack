package org.example.logitrack.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.logitrack.entities.dto.colis.ColisDTO;
import org.example.logitrack.entities.dto.colis.UpdateStatutDTO;
import org.example.logitrack.entities.enums.ColisStatut;
import org.example.logitrack.entities.enums.ColisType;
import org.example.logitrack.entities.models.User;
import org.example.logitrack.services.IColisService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transporteur/colis")
@RequiredArgsConstructor
@PreAuthorize("hasRole('TRANSPORTEUR')")
class TransporteurColisController {

    private final IColisService colisService;

    @GetMapping
    public ResponseEntity<Page<ColisDTO>> getMyColis(
            @RequestParam(required = false) ColisType type,
            @RequestParam(required = false) ColisStatut statut,
            @AuthenticationPrincipal User currentUser,
            Pageable pageable) {
        return ResponseEntity.ok(colisService.getTransporteurColis(currentUser, type, statut, pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ColisDTO>> searchMyColisByAdresse(
            @RequestParam String adresse,
            @AuthenticationPrincipal User currentUser,
            Pageable pageable) {
        return ResponseEntity.ok(colisService.searchTransporteurColisByAdresse(currentUser, adresse, pageable));
    }

    @PatchMapping("/{id}/statut")
    public ResponseEntity<ColisDTO> updateStatut(
            @PathVariable String id,
            @Valid @RequestBody UpdateStatutDTO dto,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(colisService.updateStatut(id, dto.statut(), currentUser));
    }
}