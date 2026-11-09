package com.codenova.vigilia.assetmonitoring.infrastructure.persistence.jpa.repositories;

import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.SerialNumber;
import com.codenova.vigilia.assetmonitoring.infrastructure.persistence.jpa.entities.SensorPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data repository for sensor persistence entities.
 */
@Repository
public interface SensorPersistenceRepository extends JpaRepository<SensorPersistenceEntity, Long> {

    boolean existsBySerialNumber(SerialNumber serialNumber);

    List<SensorPersistenceEntity> findAllByEquipmentId(Long equipmentId);
}
