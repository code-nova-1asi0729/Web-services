package com.codenova.vigilia.assetmonitoring.domain.model.commands;

import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.DeviceStatus;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.DeviceType;

import java.time.LocalDate;


public record ModifyDeviceCommand(Long deviceId, String name, DeviceType type, String location,
                                  LocalDate installationDate, DeviceStatus status) {

    public ModifyDeviceCommand {
        if (deviceId == null || deviceId <= 0) throw new IllegalArgumentException("device.error.id.invalid");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("device.error.name.notBlank");
        if (type == null) throw new IllegalArgumentException("device.error.type.notNull");
        if (location == null || location.isBlank()) throw new IllegalArgumentException("device.error.location.notBlank");
        if (installationDate == null || installationDate.isAfter(LocalDate.now())) throw new IllegalArgumentException("device.error.installationDate.invalid");
        if (status == null) throw new IllegalArgumentException("device.error.status.notNull");
    }
}