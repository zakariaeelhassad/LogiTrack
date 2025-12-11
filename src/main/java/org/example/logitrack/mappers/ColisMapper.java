package org.example.logitrack.mappers;

import org.example.logitrack.entities.dto.colis.ColisDTO;
import org.example.logitrack.entities.dto.colis.CreateColisDTO;
import org.example.logitrack.entities.models.Colis;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ColisMapper {
    ColisDTO toDto(Colis entity);
    Colis toEntity(CreateColisDTO dto);
}