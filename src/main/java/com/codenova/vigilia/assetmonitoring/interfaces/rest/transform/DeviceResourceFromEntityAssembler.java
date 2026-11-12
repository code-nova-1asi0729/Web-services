package com.codenova.vigilia.assetmonitoring.interfaces.rest.transform;

import com.codenova.vigilia.assetmonitoring.domain.model.aggregates.Device;
import com.codenova.vigilia.assetmonitoring.interfaces.rest.resources.DeviceResource;

public class DeviceResourceFromEntityAssembler {

    public static DeviceResource toResourceFromEntity(Device entity) {
        return new DeviceResource(
                entity.getId(),
                entity.getBuildingId(),
                entity.getCode().value(),
                entity.getName(),
                entity.getType().name(),
                entity.getLocation(),
                entity.getInstallationDate(),
                entity.getStatus().name());
    }
}