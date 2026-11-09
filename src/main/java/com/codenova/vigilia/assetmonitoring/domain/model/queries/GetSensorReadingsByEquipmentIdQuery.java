package com.codenova.vigilia.assetmonitoring.domain.model.queries;

public record GetSensorReadingsByEquipmentIdQuery(Long equipmentId) {
    public GetSensorReadingsByEquipmentIdQuery {
        if (equipmentId == null || equipmentId <= 0) throw new IllegalArgumentException("reading.error.equipmentId.invalid");
    }
}
