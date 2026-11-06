package com.codenova.vigilia.assetmonitoring.domain.model.commands;

import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.EquipmentCode;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.EquipmentType;

import java.time.LocalDate;

public record CreateEquipmentCommand(Long buildingId, EquipmentCode code, String name, EquipmentType type,
                                     String location, LocalDate installationDate) {

    public CreateEquipmentCommand {
        if (buildingId == null || buildingId <= 0) throw new IllegalArgumentException("equipment.error.buildingId.invalid");
        if (code == null) throw new IllegalArgumentException("equipment.error.code.notBlank");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("equipment.error.name.notBlank");
        if (type == null) throw new IllegalArgumentException("equipment.error.type.notNull");
        if (location == null || location.isBlank()) throw new IllegalArgumentException("equipment.error.location.notBlank");
        if (installationDate == null || installationDate.isAfter(LocalDate.now())) throw new IllegalArgumentException("equipment.error.installationDate.invalid");
    }
}