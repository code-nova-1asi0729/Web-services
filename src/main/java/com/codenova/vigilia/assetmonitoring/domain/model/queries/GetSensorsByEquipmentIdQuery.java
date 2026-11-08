package com.codenova.vigilia.assetmonitoring.domain.model.queries;

public record GetSensorsByEquipmentIdQuery(Long equipmentId) {
    public GetSensorsByEquipmentIdQuery {
        if (equipmentId == null || equipmentId <= 0) throw new IllegalArgumentException("sensor.error.equipmentId.invalid");
    }
}
