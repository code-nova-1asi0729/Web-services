package com.codenova.vigilia.assetmonitoring.domain.model.commands;
/**
 * Command to delete a building
 */
public record DeleteBuildingCommand(Long buildingId) {
    public DeleteBuildingCommand {
        if (buildingId == null || buildingId <= 0) throw new IllegalArgumentException("building.error.id.invalid");
    }
}