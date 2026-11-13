package com.codenova.vigilia.assetmonitoring.infrastructure.persistence.jpa.assemblers;

import com.codenova.vigilia.assetmonitoring.domain.model.aggregates.Sensor;
import com.codenova.vigilia.assetmonitoring.infrastructure.persistence.jpa.entities.SensorPersistenceEntity;

/**
 * Converts between the Sensor aggregate and its persistence entity.
 */
public final class SensorPersistenceAssembler {

    private SensorPersistenceAssembler() {
    }

    public static Sensor toDomainFromPersistence(SensorPersistenceEntity entity) {
        return Sensor.reconstitute(
                entity.getId(),
                entity.getDeviceId(),
                entity.getSerialNumber(),
                entity.getModel(),
                entity.getStatus(),
                entity.getLastSeenAt());
    }

    public static SensorPersistenceEntity toPersistenceFromDomain(Sensor sensor) {
        var entity = new SensorPersistenceEntity();
        // A new sensor has no id yet, so JPA generates it
        if (sensor.getId() != null) entity.setId(sensor.getId());
        entity.setDeviceId(sensor.getDeviceId());
        entity.setSerialNumber(sensor.getSerialNumber());
        entity.setModel(sensor.getModel());
        entity.setStatus(sensor.getStatus());
        entity.setLastSeenAt(sensor.getLastSeenAt());
        return entity;
    }
}
