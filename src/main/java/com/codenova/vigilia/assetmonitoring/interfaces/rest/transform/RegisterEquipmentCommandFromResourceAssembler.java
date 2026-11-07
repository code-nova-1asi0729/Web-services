package com.codenova.vigilia.assetmonitoring.interfaces.rest.transform;

import com.codenova.vigilia.assetmonitoring.domain.model.commands.RegisterEquipmentCommand;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.EquipmentCode;
import com.codenova.vigilia.assetmonitoring.interfaces.rest.resources.RegisterEquipmentResource;


public class RegisterEquipmentCommandFromResourceAssembler {
    public static RegisterEquipmentCommand toCommandFromResource(RegisterEquipmentResource resource){
        return new RegisterEquipmentCommand(
                resource.buildingId(),
                new EquipmentCode(resource.code()),
                resource.name(),
                resource.type(),
                resource.location(),
                resource.installationDate()
        );
    }
}
