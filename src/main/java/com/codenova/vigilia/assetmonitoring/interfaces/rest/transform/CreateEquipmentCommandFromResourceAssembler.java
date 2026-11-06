package com.codenova.vigilia.assetmonitoring.interfaces.rest.transform;

import com.codenova.vigilia.assetmonitoring.domain.model.commands.CreateEquipmentCommand;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.EquipmentCode;
import com.codenova.vigilia.assetmonitoring.interfaces.rest.resources.CreateEquipmentResource;


public class CreateEquipmentCommandFromResourceAssembler {
    public static CreateEquipmentCommand toCommandFromResource(CreateEquipmentResource resource){
        return new CreateEquipmentCommand(
                resource.buildingId(),
                new EquipmentCode(resource.code()),
                resource.name(),
                resource.type(),
                resource.location(),
                resource.installationDate()
        );
    }
}
