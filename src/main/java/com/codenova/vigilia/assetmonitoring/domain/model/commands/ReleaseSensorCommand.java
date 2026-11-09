package com.codenova.vigilia.assetmonitoring.domain.model.commands;

/**
 * Command to release a sensor from its equipment, so it can be assigned to another one.
 */
public record ReleaseSensorCommand(Long sensorId) {

    public ReleaseSensorCommand {
        if (sensorId == null || sensorId <= 0) throw new IllegalArgumentException("sensor.error.id.invalid");
    }
}
