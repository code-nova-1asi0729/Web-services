package com.codenova.vigilia.assetmonitoring.domain.model.commands;

import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.BuildingCode;
/**
 * Command to register a new building in Vigilia.
 */
public record RegisterBuildingCommand(String name, BuildingCode code, String address, String district, Integer totalUnits) {
    public RegisterBuildingCommand {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("building.error.name.notBlank");
        if (code == null) throw new IllegalArgumentException("building.error.code.notBlank");
        if (address == null || address.isBlank()) throw new IllegalArgumentException("building.error.address.notBlank");
        if (district == null || district.isBlank()) throw new IllegalArgumentException("building.error.district.notBlank");
        if (totalUnits == null || totalUnits <= 0) throw new IllegalArgumentException("building.error.totalUnits.positive");
    }
}
