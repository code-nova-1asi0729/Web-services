package com.codenova.vigilia.assetmonitoring.interfaces.rest.transform;

import com.codenova.vigilia.assetmonitoring.domain.model.commands.ModifyDeviceCommand;
import com.codenova.vigilia.assetmonitoring.interfaces.rest.resources.ModifyDeviceResource;

public class ModifyDeviceCommandFromResourceAssembler {
    public static ModifyDeviceCommand toCommandFromResource(Long deviceId, ModifyDeviceResource resource){
        return new ModifyDeviceCommand(
                deviceId,
                resource.name(),
                resource.type(),
                resource.location(),
                resource.installationDate(),
                resource.status()
        );
    }
}
