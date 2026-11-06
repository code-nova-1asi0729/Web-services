package com.codenova.vigilia.assetmonitoring.domain.model.queries;

public record GetEquipmentByBuildingIdQuery(Long buildingId) {
    public GetEquipmentByBuildingIdQuery{
        if (buildingId == null || buildingId <= 0)throw new IllegalArgumentException("equipment.error.buildingId.invalid");
    }
}
