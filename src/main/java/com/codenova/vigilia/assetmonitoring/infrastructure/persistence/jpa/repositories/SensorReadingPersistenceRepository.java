package com.codenova.vigilia.assetmonitoring.infrastructure.persistence.jpa.repositories;

import com.codenova.vigilia.assetmonitoring.infrastructure.persistence.jpa.entities.SensorReadingPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data repository for sensor reading persistence entities.
 */
@Repository
public interface SensorReadingPersistenceRepository extends JpaRepository<SensorReadingPersistenceEntity, Long> {

    List<SensorReadingPersistenceEntity> findAllByOrderByRecordedAtDesc();

    List<SensorReadingPersistenceEntity> findAllByDeviceIdOrderByRecordedAtDesc(Long deviceId);
}
