package org.example.logitrack.mappers;

import org.example.logitrack.entities.dto.user.CreateUserDTO;
import org.example.logitrack.entities.dto.user.UserDTO;
import org.example.logitrack.entities.models.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDto(User user);
    User toEntity(CreateUserDTO dto);
}