package com.codenova.vigilia.assetmonitoring.interfaces.rest.transform;

import com.codenova.vigilia.assetmonitoring.domain.model.aggregates.Sensor;
import com.codenova.vigilia.assetmonitoring.interfaces.rest.resources.SensorResource;

public class SensorResourceFromEntityAssembler {

    public static SensorResource toResourceFromEntity(Sensor entity) {
        return new SensorResource(
                entity.getId(),
                entity.getEquipmentId(),
                entity.getSerialNumber().value(),
                entity.getModel(),
                entity.getStatus().name(),
                entity.getLastSeenAt());
    }
}
