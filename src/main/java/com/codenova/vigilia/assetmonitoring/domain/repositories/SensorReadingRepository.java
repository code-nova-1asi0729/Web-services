package com.codenova.vigilia.assetmonitoring.domain.repositories;

import com.codenova.vigilia.assetmonitoring.domain.model.aggregates.SensorReading;

import java.util.List;
import java.util.Optional;

/**
 * Domain contract to store and retrieve sensor readings. Lists come from the most recent to the oldest.
 */
public interface SensorReadingRepository {

    List<SensorReading> findAll();

    List<SensorReading> findAllByDeviceId(Long deviceId);

    Optional<SensorReading> findById(Long id);

    SensorReading save(SensorReading reading);
}
