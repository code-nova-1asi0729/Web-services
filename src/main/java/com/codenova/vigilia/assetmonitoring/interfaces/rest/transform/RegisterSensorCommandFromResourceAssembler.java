package com.codenova.vigilia.assetmonitoring.interfaces.rest.transform;

import com.codenova.vigilia.assetmonitoring.domain.model.commands.RegisterSensorCommand;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.SerialNumber;
import com.codenova.vigilia.assetmonitoring.interfaces.rest.resources.RegisterSensorResource;

public class RegisterSensorCommandFromResourceAssembler {

    public static RegisterSensorCommand toCommandFromResource(RegisterSensorResource resource) {
        return new RegisterSensorCommand(new SerialNumber(resource.serialNumber()), resource.model());
    }
}
