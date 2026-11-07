package com.codenova.vigilia.assetmonitoring.interfaces.rest.transform;

import com.codenova.vigilia.assetmonitoring.domain.model.aggregates.Building;
import com.codenova.vigilia.assetmonitoring.interfaces.rest.resources.BuildingResource;
/**
 * Builds a BuildingResource from the Building aggregate.
 */
public class BuildingResourceFromEntityAssembler {
    public static BuildingResource toResourceFromEntity(Building entity) {
        return new BuildingResource(
                entity.getId(),
                entity.getName(),
                entity.getCode().value(),
                entity.getAddress(),
                entity.getDistrict(),
                entity.getTotalUnits(),
                entity.getStatus().name());
    }
}