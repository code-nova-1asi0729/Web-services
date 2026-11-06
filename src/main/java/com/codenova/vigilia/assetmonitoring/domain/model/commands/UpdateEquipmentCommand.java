package com.codenova.vigilia.assetmonitoring.domain.model.commands;

import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.EquipmentStatus;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.EquipmentType;

import java.time.LocalDate;


public record UpdateEquipmentCommand(Long equipmentId, String name, EquipmentType type, String location,
                                     LocalDate installationDate, EquipmentStatus status) {

    public UpdateEquipmentCommand {
        if (equipmentId == null || equipmentId <= 0) throw new IllegalArgumentException("equipment.error.id.invalid");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("equipment.error.name.notBlank");
        if (type == null) throw new IllegalArgumentException("equipment.error.type.notNull");
        if (location == null || location.isBlank()) throw new IllegalArgumentException("equipment.error.location.notBlank");
        if (installationDate == null || installationDate.isAfter(LocalDate.now())) throw new IllegalArgumentException("equipment.error.installationDate.invalid");
        if (status == null) throw new IllegalArgumentException("equipment.error.status.notNull");
    }
}