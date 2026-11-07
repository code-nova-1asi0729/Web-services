package com.codenova.vigilia.assetmonitoring.interfaces.rest.transform;

import com.codenova.vigilia.assetmonitoring.domain.model.commands.ModifyEquipmentCommand;
import com.codenova.vigilia.assetmonitoring.interfaces.rest.resources.ModifyEquipmentResource;

public class ModifyEquipmentCommandFromResourceAssembler {
    public static ModifyEquipmentCommand toCommandFromResource(Long equipmentId, ModifyEquipmentResource resource){
        return new ModifyEquipmentCommand(
                equipmentId,
                resource.name(),
                resource.type(),
                resource.location(),
                resource.installationDate(),
                resource.status()
        );
    }
}
