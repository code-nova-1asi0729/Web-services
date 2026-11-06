package com.codenova.vigilia.incidents.infrastructure.persistence.jpa.repositories;

import com.codenova.vigilia.incidents.infrastructure.persistence.jpa.entities.IncidentPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data repository for incident persistence entities.
 */
@Repository
public interface IncidentPersistenceRepository extends JpaRepository<IncidentPersistenceEntity, Long> {
    List<IncidentPersistenceEntity> findAllByBuildingId(Long buildingId);
}
