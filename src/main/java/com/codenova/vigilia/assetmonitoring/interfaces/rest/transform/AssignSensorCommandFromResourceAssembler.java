package com.codenova.vigilia.assetmonitoring.interfaces.rest.transform;

import com.codenova.vigilia.assetmonitoring.domain.model.commands.AssignSensorCommand;
import com.codenova.vigilia.assetmonitoring.interfaces.rest.resources.AssignSensorResource;

public class AssignSensorCommandFromResourceAssembler {

    public static AssignSensorCommand toCommandFromResource(Long sensorId, AssignSensorResource resource) {
        return new AssignSensorCommand(sensorId, resource.equipmentId());
    }
}
