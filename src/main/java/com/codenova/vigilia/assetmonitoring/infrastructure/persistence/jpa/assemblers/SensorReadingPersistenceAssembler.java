package com.codenova.vigilia.assetmonitoring.infrastructure.persistence.jpa.assemblers;

import com.codenova.vigilia.assetmonitoring.domain.model.aggregates.SensorReading;
import com.codenova.vigilia.assetmonitoring.infrastructure.persistence.jpa.entities.SensorReadingPersistenceEntity;

/**
 * Converts between the SensorReading aggregate and its persistence entity.
 */
public final class SensorReadingPersistenceAssembler {

    private SensorReadingPersistenceAssembler() {
    }

    public static SensorReading toDomainFromPersistence(SensorReadingPersistenceEntity entity) {
        return SensorReading.reconstitute(
                entity.getId(),
                entity.getSensorId(),
                entity.getDeviceId(),
                entity.getMetric(),
                entity.getValue(),
                entity.getUnit(),
                entity.getStatus(),
                entity.getRecordedAt());
    }

    public static SensorReadingPersistenceEntity toPersistenceFromDomain(SensorReading reading) {
        var entity = new SensorReadingPersistenceEntity();
        if (reading.getId() != null) entity.setId(reading.getId());
        entity.setSensorId(reading.getSensorId());
        entity.setDeviceId(reading.getDeviceId());
        entity.setMetric(reading.getMetric());
        entity.setValue(reading.getValue());
        entity.setUnit(reading.getUnit());
        entity.setStatus(reading.getStatus());
        entity.setRecordedAt(reading.getRecordedAt());
        return entity;
    }
}
