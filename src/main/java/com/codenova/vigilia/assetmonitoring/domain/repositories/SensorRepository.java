package com.codenova.vigilia.assetmonitoring.domain.repositories;

import com.codenova.vigilia.assetmonitoring.domain.model.aggregates.Sensor;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.SerialNumber;

import java.util.List;
import java.util.Optional;

/**
 * Domain contract to store and retrieve IoT sensors.
 */
public interface SensorRepository {

    List<Sensor> findAll();

    List<Sensor> findAllByEquipmentId(Long equipmentId);

    Optional<Sensor> findById(Long id);

    boolean existsBySerialNumber(SerialNumber serialNumber);

    Sensor save(Sensor sensor);
}
