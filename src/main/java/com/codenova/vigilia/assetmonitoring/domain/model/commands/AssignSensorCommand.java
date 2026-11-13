package com.codenova.vigilia.assetmonitoring.domain.model.commands;

/**
 * Command to link a sensor to the device it will measure.
 */
public record AssignSensorCommand(Long sensorId, Long deviceId) {

    public AssignSensorCommand {
        if (sensorId == null || sensorId <= 0) throw new IllegalArgumentException("sensor.error.id.invalid");
        if (deviceId == null || deviceId <= 0) throw new IllegalArgumentException("sensor.error.deviceId.invalid");
    }
}
