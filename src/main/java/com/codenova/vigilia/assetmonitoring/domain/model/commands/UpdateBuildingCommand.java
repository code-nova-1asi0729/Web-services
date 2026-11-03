package com.codenova.vigilia.assetmonitoring.domain.model.commands;

import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.BuildingStatus;
/**
 * Command to update the general data of a building
 */
public record UpdateBuildingCommand(Long buildingId, String name, String address, String district,
                                    Integer totalUnits, BuildingStatus status) {
    public UpdateBuildingCommand {
        if (buildingId == null || buildingId <= 0) throw new IllegalArgumentException("building.error.id.invalid");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("building.error.name.notBlank");
        if (address == null || address.isBlank()) throw new IllegalArgumentException("building.error.address.notBlank");
        if (district == null || district.isBlank()) throw new IllegalArgumentException("building.error.district.notBlank");
        if (totalUnits == null || totalUnits <= 0) throw new IllegalArgumentException("building.error.totalUnits.positive");
        if (status == null) throw new IllegalArgumentException("building.error.status.notNull");
    }
}