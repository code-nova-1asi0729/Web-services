package com.codenova.vigilia.assetmonitoring.domain.model.queries;

public record GetDevicesByBuildingIdQuery(Long buildingId) {
    public GetDevicesByBuildingIdQuery {
        if (buildingId == null || buildingId <= 0)throw new IllegalArgumentException("device.error.buildingId.invalid");
    }
}
