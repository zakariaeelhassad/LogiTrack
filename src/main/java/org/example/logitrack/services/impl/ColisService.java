package org.example.logitrack.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.logitrack.entities.dto.colis.ColisDTO;
import org.example.logitrack.entities.dto.colis.CreateColisDTO;
import org.example.logitrack.entities.dto.colis.UpdateColisDTO;
import org.example.logitrack.entities.enums.ColisStatut;
import org.example.logitrack.entities.enums.ColisType;
import org.example.logitrack.entities.enums.Role;
import org.example.logitrack.entities.enums.Specialite;
import org.example.logitrack.entities.models.Colis;
import org.example.logitrack.entities.models.User;
import org.example.logitrack.mappers.ColisMapper;
import org.example.logitrack.repositories.ColisRepository;
import org.example.logitrack.repositories.UserRepository;
import org.example.logitrack.services.IColisService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ColisService implements IColisService {

    private final ColisRepository colisRepository;
    private final UserRepository userRepository;
    private final ColisMapper colisMapper;

    public Page<ColisDTO> getAllColis(ColisType type, ColisStatut statut, Pageable pageable) {
        Page<Colis> colis;

        if (type != null && statut != null) {
            colis = colisRepository.findByTypeAndStatut(type, statut, pageable);
        } else if (type != null) {
            colis = colisRepository.findByType(type, pageable);
        } else if (statut != null) {
            colis = colisRepository.findByStatut(statut, pageable);
        } else {
            colis = colisRepository.findAll(pageable);
        }

        return colis.map(colisMapper::toDto);
    }

    public Page<ColisDTO> getTransporteurColis(User transporteur, ColisType type, ColisStatut statut, Pageable pageable) {
        Page<Colis> colis;

        if (type != null && statut != null) {
            colis = colisRepository.findByTransporteurAndTypeAndStatut(transporteur, type, statut, pageable);
        } else if (type != null) {
            colis = colisRepository.findByTransporteurAndType(transporteur, type, pageable);
        } else if (statut != null) {
            colis = colisRepository.findByTransporteurAndStatut(transporteur, statut, pageable);
        } else {
            colis = colisRepository.findByTransporteur(transporteur, pageable);
        }

        return colis.map(colisMapper::toDto);
    }

    // ADMIN - Rechercher par adresse
    public Page<ColisDTO> searchByAdresse(String adresse, Pageable pageable) {
        return colisRepository.findByAdresseDestinationContaining(adresse, pageable)
                .map(colisMapper::toDto);
    }

    // TRANSPORTEUR - Rechercher ses colis par adresse
    public Page<ColisDTO> searchTransporteurColisByAdresse(User transporteur, String adresse, Pageable pageable) {
        return colisRepository.findByTransporteurAndAdresseDestinationContaining(transporteur, adresse, pageable)
                .map(colisMapper::toDto);
    }

    public ColisDTO createColis(CreateColisDTO dto) {
        validateColisFields(dto.type(), dto.instructionsManutention(), dto.temperatureMin(), dto.temperatureMax());

        Colis colis = colisMapper.toEntity(dto);
        Colis saved = colisRepository.save(colis);
        return colisMapper.toDto(saved);
    }

    public ColisDTO assignTransporteur(String colisId, String transporteurId) {
        Colis colis = colisRepository.findById(colisId)
                .orElseThrow(() -> new IllegalArgumentException("Colis not found"));

        User transporteur = userRepository.findById(transporteurId)
                .orElseThrow(() -> new IllegalArgumentException("Transporteur not found"));

        if (transporteur.getRole() != Role.TRANSPORTEUR) {
            throw new IllegalArgumentException("User is not a transporteur");
        }

        if (!transporteur.getActive()) {
            throw new IllegalArgumentException("Transporteur is not active");
        }

        if (!isSpecialiteMatching(colis.getType(), transporteur.getSpecialite())) {
            throw new IllegalArgumentException("Transporteur specialite does not match colis type");
        }

        colis.setTransporteur(transporteur);
        Colis updated = colisRepository.save(colis);
        return colisMapper.toDto(updated);
    }

    // ADMIN - Modifier un colis
    public ColisDTO updateColis(String id, UpdateColisDTO dto) {
        Colis colis = colisRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Colis not found"));

        Optional.ofNullable(dto.type()).ifPresent(colis::setType);
        Optional.ofNullable(dto.poids()).ifPresent(colis::setPoids);
        Optional.ofNullable(dto.adresseDestination()).ifPresent(colis::setAdresseDestination);
        Optional.ofNullable(dto.instructionsManutention()).ifPresent(colis::setInstructionsManutention);
        Optional.ofNullable(dto.temperatureMin()).ifPresent(colis::setTemperatureMin);
        Optional.ofNullable(dto.temperatureMax()).ifPresent(colis::setTemperatureMax);

        validateColisFields(colis.getType(), colis.getInstructionsManutention(), colis.getTemperatureMin(), colis.getTemperatureMax());

        Colis updated = colisRepository.save(colis);
        return colisMapper.toDto(updated);
    }

    public ColisDTO updateStatut(String id, ColisStatut statut, User currentUser) {
        Colis colis = colisRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Colis not found"));

        if (currentUser.getRole() == Role.TRANSPORTEUR) {
            if (colis.getTransporteur() == null || !colis.getTransporteur().getId().equals(currentUser.getId())) {
                throw new IllegalArgumentException("Transporteur is not assigned to this colis");
            }
        }

        colis.setStatut(statut);
        Colis updated = colisRepository.save(colis);
        return colisMapper.toDto(updated);
    }

    // ADMIN - Supprimer un colis
    public void deleteColis(String id) {
        if (!colisRepository.existsById(id)) {
            throw new IllegalArgumentException("Colis not found");
        }
        colisRepository.deleteById(id);
    }

    private boolean isSpecialiteMatching(ColisType type, Specialite specialite) {
        if (specialite == null) return false;
        return type.name().equals(specialite.name());
    }

    private void validateColisFields(ColisType type, String instructions, Double tempMin, Double tempMax) {
        if (type == ColisType.FRAGILE && (instructions == null || instructions.isBlank())) {
            throw new IllegalArgumentException("Instructions de manutention required for FRAGILE colis");
        }

        if (type == ColisType.FRIGO) {
            if (tempMin == null || tempMax == null) {
                throw new IllegalArgumentException("Temperature min and max required for FRIGO colis");
            }
            if (tempMin >= tempMax) {
                throw new IllegalArgumentException("Temperature min must be less than temperature max");
            }
        }
    }
}
