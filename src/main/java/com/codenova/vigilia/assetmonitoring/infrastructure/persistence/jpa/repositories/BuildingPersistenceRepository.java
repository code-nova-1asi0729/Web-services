package com.codenova.vigilia.assetmonitoring.infrastructure.persistence.jpa.repositories;

import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.BuildingCode;
import com.codenova.vigilia.assetmonitoring.infrastructure.persistence.jpa.entities.BuildingPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data repository for building persistence entities.
 */
@Repository
public interface BuildingPersistenceRepository extends JpaRepository<BuildingPersistenceEntity, Long> {
    boolean existsByCode(BuildingCode code);
}
