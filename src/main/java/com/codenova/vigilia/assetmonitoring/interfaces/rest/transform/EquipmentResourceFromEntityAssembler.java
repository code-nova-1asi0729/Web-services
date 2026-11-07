package com.codenova.vigilia.assetmonitoring.interfaces.rest.transform;

import com.codenova.vigilia.assetmonitoring.domain.model.aggregates.CriticalEquipment;
import com.codenova.vigilia.assetmonitoring.interfaces.rest.resources.EquipmentResource;

public class EquipmentResourceFromEntityAssembler {

    public static EquipmentResource toResourceFromEntity(CriticalEquipment entity) {
        return new EquipmentResource(
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