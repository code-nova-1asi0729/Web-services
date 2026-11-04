package com.codenova.vigilia.incidents.infrastructure.persistence.jpa.repositories;

import com.codenova.vigilia.incidents.domain.model.aggregates.Incident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 * Persistence access for incidents
 */
@Repository
public interface IncidentRepository extends JpaRepository<Incident, Long> {
    List<Incident> findAllByBuildingId(Long buildingId);
}