package com.codenova.vigilia.assetmonitoring.interfaces.rest.transform;

import com.codenova.vigilia.assetmonitoring.domain.model.aggregates.SensorReading;
import com.codenova.vigilia.assetmonitoring.interfaces.rest.resources.SensorReadingResource;

public class SensorReadingResourceFromEntityAssembler {

    public static SensorReadingResource toResourceFromEntity(SensorReading entity) {
        return new SensorReadingResource(
                entity.getId(),
                entity.getSensorId(),
                entity.getEquipmentId(),
                entity.getMetric().name(),
                entity.getValue(),
                entity.getUnit(),
                entity.getStatus().name(),
                entity.getRecordedAt());
    }
}
