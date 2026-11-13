package com.codenova.vigilia.assetmonitoring.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "Sensor reading")
public record SensorReadingResource(
        @Schema(description = "Reading identifier", example = "1")
        Long id,

        @Schema(description = "Sensor that sent the reading", example = "1")
        Long sensorId,

        @Schema(description = "Device measured", example = "1")
        Long deviceId,

        @Schema(description = "Measured magnitude: VIBRATION, TEMPERATURE, HUMIDITY, POWER_CONSUMPTION, CURRENT or PRESSURE", example = "CURRENT")
        String metric,

        @Schema(description = "Measured value", example = "2.3")
        Double value,

        @Schema(description = "Unit of the value", example = "mm/s")
        String unit,

        @Schema(description = "Whether the value is physically possible", example = "VALID")
        String status,

        @Schema(description = "When the sensor measured the value", example = "2026-11-07T12:00:00Z")
        Instant recordedAt) {}
