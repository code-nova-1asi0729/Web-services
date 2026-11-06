package com.codenova.vigilia.assetmonitoring.interfaces.rest.transform;

import com.codenova.vigilia.assetmonitoring.domain.model.commands.UpdateEquipmentCommand;
import com.codenova.vigilia.assetmonitoring.interfaces.rest.resources.UpdateEquipmentResource;

public class UpdateEquipmentCommandFromResourceAssembler {
    public static UpdateEquipmentCommand toCommandFromResource(Long equipmentId, UpdateEquipmentResource resource){
        return new UpdateEquipmentCommand(
                equipmentId,
                resource.name(),
                resource.type(),
                resource.location(),
                resource.installationDate(),
                resource.status()
        );
    }
}
