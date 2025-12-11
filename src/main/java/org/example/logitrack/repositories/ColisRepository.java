package org.example.logitrack.repositories;


import org.example.logitrack.entities.enums.ColisStatut;
import org.example.logitrack.entities.enums.ColisType;
import org.example.logitrack.entities.models.Colis;
import org.example.logitrack.entities.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ColisRepository extends MongoRepository<Colis, String> {

    Page<Colis> findByType(ColisType type, Pageable pageable);

    Page<Colis> findByStatut(ColisStatut statut, Pageable pageable);

    Page<Colis> findByTypeAndStatut(ColisType type, ColisStatut statut, Pageable pageable);

    Page<Colis> findByAdresseDestinationContaining(String adresse, Pageable pageable);

    Page<Colis> findByTransporteur(User transporteur, Pageable pageable);

    Page<Colis> findByTransporteurAndType(User transporteur, ColisType type, Pageable pageable);

    Page<Colis> findByTransporteurAndStatut(User transporteur, ColisStatut statut, Pageable pageable);

    Page<Colis> findByTransporteurAndTypeAndStatut(User transporteur, ColisType type, ColisStatut statut, Pageable pageable);

    Page<Colis> findByTransporteurAndAdresseDestinationContaining(User transporteur, String adresse, Pageable pageable);
}

