package com.codenova.vigilia.assetmonitoring.domain.model.commands;

import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.DeviceCode;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.DeviceType;

import java.time.LocalDate;

public record RegisterDeviceCommand(Long buildingId, DeviceCode code, String name, DeviceType type,
                                    String location, LocalDate installationDate) {

    public RegisterDeviceCommand {
        if (buildingId == null || buildingId <= 0) throw new IllegalArgumentException("device.error.buildingId.invalid");
        if (code == null) throw new IllegalArgumentException("device.error.code.notBlank");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("device.error.name.notBlank");
        if (type == null) throw new IllegalArgumentException("device.error.type.notNull");
        if (location == null || location.isBlank()) throw new IllegalArgumentException("device.error.location.notBlank");
        if (installationDate == null || installationDate.isAfter(LocalDate.now())) throw new IllegalArgumentException("device.error.installationDate.invalid");
    }
}