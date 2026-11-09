package com.codenova.vigilia.assetmonitoring.domain.model.commands;

/**
 * Command to link a sensor to the critical equipment it will measure.
 */
public record AssignSensorCommand(Long sensorId, Long equipmentId) {

    public AssignSensorCommand {
        if (sensorId == null || sensorId <= 0) throw new IllegalArgumentException("sensor.error.id.invalid");
        if (equipmentId == null || equipmentId <= 0) throw new IllegalArgumentException("sensor.error.equipmentId.invalid");
    }
}
