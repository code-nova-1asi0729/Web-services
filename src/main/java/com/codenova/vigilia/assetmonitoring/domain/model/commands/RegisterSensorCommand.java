package com.codenova.vigilia.assetmonitoring.domain.model.commands;

import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.SerialNumber;

/**
 * Command to register a new IoT sensor in the inventory, still without a device.
 */
public record RegisterSensorCommand(SerialNumber serialNumber, String model) {

    public RegisterSensorCommand {
        if (serialNumber == null) throw new IllegalArgumentException("sensor.error.serialNumber.notBlank");
        if (model == null || model.isBlank()) throw new IllegalArgumentException("sensor.error.model.notBlank");
    }
}
