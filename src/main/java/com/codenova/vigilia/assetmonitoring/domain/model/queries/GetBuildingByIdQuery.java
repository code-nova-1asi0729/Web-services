package com.codenova.vigilia.assetmonitoring.domain.model.queries;
/**
 * Query to find a building by its identifier.
 */
public record GetBuildingByIdQuery(Long buildingId) {
    public GetBuildingByIdQuery {
        if (buildingId == null || buildingId <= 0) throw new IllegalArgumentException("building.error.id.invalid");
    }
}