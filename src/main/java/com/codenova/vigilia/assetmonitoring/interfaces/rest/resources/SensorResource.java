package com.codenova.vigilia.assetmonitoring.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "IoT sensor")
public record SensorResource(
        @Schema(description = "Sensor identifier", example = "1")
        Long id,

        @Schema(description = "Device the sensor measures, or null if it is not assigned", example = "1")
        Long deviceId,

        @Schema(description = "Serial number", example = "VS-1001")
        String serialNumber,

        @Schema(description = "Sensor model", example = "VibroSense V2")
        String model,

        @Schema(description = "Connection status", example = "ASSIGNED")
        String status,

        @Schema(description = "Last time the sensor sent a reading", example = "2026-11-07T12:00:00Z")
        Instant lastSeenAt) {}
