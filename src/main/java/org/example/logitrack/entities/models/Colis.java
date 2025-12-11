package org.example.logitrack.entities.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.logitrack.entities.enums.ColisStatut;
import org.example.logitrack.entities.enums.ColisType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "colis")
public class Colis {

    @Id
    private String id;

    private ColisType type;

    private Double poids;

    private String adresseDestination;

    private ColisStatut statut = ColisStatut.EN_ATTENTE;

    @DBRef
    private User transporteur;

    private String instructionsManutention;

    private Double temperatureMin;
    private Double temperatureMax;

}