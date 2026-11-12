package com.codenova.vigilia.assetmonitoring.interfaces.rest.transform;

import com.codenova.vigilia.assetmonitoring.domain.model.commands.RegisterDeviceCommand;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.DeviceCode;
import com.codenova.vigilia.assetmonitoring.interfaces.rest.resources.RegisterDeviceResource;


public class RegisterDeviceCommandFromResourceAssembler {
    public static RegisterDeviceCommand toCommandFromResource(RegisterDeviceResource resource){
        return new RegisterDeviceCommand(
                resource.buildingId(),
                new DeviceCode(resource.code()),
                resource.name(),
                resource.type(),
                resource.location(),
                resource.installationDate()
        );
    }
}
