package org.example.logitrack.services;

import org.example.logitrack.entities.dto.colis.ColisDTO;
import org.example.logitrack.entities.dto.colis.CreateColisDTO;
import org.example.logitrack.entities.dto.colis.UpdateColisDTO;
import org.example.logitrack.entities.enums.ColisStatut;
import org.example.logitrack.entities.enums.ColisType;
import org.example.logitrack.entities.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IColisService {
    Page<ColisDTO> getAllColis(ColisType type, ColisStatut statut, Pageable pageable);

    Page<ColisDTO> getTransporteurColis(User transporteur, ColisType type, ColisStatut statut, Pageable pageable);

    Page<ColisDTO> searchByAdresse(String adresse, Pageable pageable);

    Page<ColisDTO> searchTransporteurColisByAdresse(User transporteur, String adresse, Pageable pageable);

    ColisDTO createColis(CreateColisDTO dto);

    ColisDTO assignTransporteur(String colisId, String transporteurId);

    ColisDTO updateColis(String id, UpdateColisDTO dto);

    ColisDTO updateStatut(String id, ColisStatut statut, User currentUser);

    void deleteColis(String id);
}
