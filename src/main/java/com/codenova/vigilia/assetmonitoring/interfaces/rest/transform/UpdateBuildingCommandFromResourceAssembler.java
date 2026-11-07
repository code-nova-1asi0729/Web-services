package com.codenova.vigilia.assetmonitoring.interfaces.rest.transform;

import com.codenova.vigilia.assetmonitoring.domain.model.commands.UpdateBuildingCommand;
import com.codenova.vigilia.assetmonitoring.interfaces.rest.resources.UpdateBuildingResource;
/**
 * Builds an UpdateBuildingCommand from its REST resource and the building id of the route
 */
public class UpdateBuildingCommandFromResourceAssembler {
    public static UpdateBuildingCommand toCommandFromResource(Long buildingId, UpdateBuildingResource resource) {
        return new UpdateBuildingCommand(
                buildingId,
                resource.name(),
                resource.address(),
                resource.district(),
                resource.totalUnits(),
                resource.status());
    }
}