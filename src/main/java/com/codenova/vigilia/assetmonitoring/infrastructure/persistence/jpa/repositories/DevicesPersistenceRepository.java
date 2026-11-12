package com.codenova.vigilia.assetmonitoring.infrastructure.persistence.jpa.repositories;

import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.DeviceCode;
import com.codenova.vigilia.assetmonitoring.infrastructure.persistence.jpa.entities.DevicePersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data repository for critical devices persistence entities.
 */
@Repository
public interface DevicesPersistenceRepository extends JpaRepository<DevicePersistenceEntity, Long> {
    boolean existsByBuildingIdAndCode(Long buildingId, DeviceCode code);

    List<DevicePersistenceEntity> findAllByBuildingId(Long buildingId);
}
