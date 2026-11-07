package com.codenova.vigilia.assetmonitoring.interfaces.rest.transform;

import com.codenova.vigilia.assetmonitoring.domain.model.commands.RegisterBuildingCommand;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.BuildingCode;
import com.codenova.vigilia.assetmonitoring.interfaces.rest.resources.RegisterBuildingResource;
/**
 * Builds a RegisterBuildingCommand from its REST resource.
 */
public class RegisterBuildingCommandFromResourceAssembler {
    public static RegisterBuildingCommand toCommandFromResource(RegisterBuildingResource resource) {
        return new RegisterBuildingCommand(
                resource.name(),
                new BuildingCode(resource.code()),
                resource.address(),
                resource.district(),
                resource.totalUnits());
    }
}