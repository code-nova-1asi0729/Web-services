package com.codenova.vigilia.incidents.domain.repositories;

import com.codenova.vigilia.incidents.domain.model.aggregates.Incident;

import java.util.List;
import java.util.Optional;

/**
 * Domain contract to store and retrieve incidents.
 */
public interface IncidentRepository {

    List<Incident> findAll();

    Optional<Incident> findById(Long id);

    List<Incident> findAllByBuildingId(Long buildingId);

    Incident save(Incident incident);
}
