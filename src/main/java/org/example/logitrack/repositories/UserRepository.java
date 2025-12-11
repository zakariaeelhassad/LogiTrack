package org.example.logitrack.repositories;


import org.example.logitrack.entities.enums.Role;
import org.example.logitrack.entities.enums.Specialite;
import org.example.logitrack.entities.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByLogin(String login);

    Optional<User> findByLoginAndActiveTrue(String login);

    Page<User> findByRole(Role role, Pageable pageable);

    Page<User> findByRoleAndSpecialite(Role role, Specialite specialite, Pageable pageable);
}