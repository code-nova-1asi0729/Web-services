package com.codenova.vigilia.assetmonitoring.domain.model.queries;

public record GetEquipmentByIdQuery(Long equipmentId) {
    public GetEquipmentByIdQuery{
        if(equipmentId == null || equipmentId <= 0) throw new IllegalArgumentException("equipment.error.id.invalid");
    }
}
