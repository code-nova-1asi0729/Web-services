package com.codenova.vigilia.assetmonitoring.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
/**
 * Response body that represents an alert.
 */
@Schema(description = "Alert")
public record AlertResource(
        @Schema(description = "Alert identifier", example = "1")
        Long id,
        @Schema(description = "Device identifier", example = "2")
        Long deviceId,
        @Schema(description = "Measured magnitude", example = "TEMPERATURE")
        String metric,
        @Schema(description = "How serious it is", example = "HIGH")
        String severity,
        @Schema(description = "Attention status", example = "ACTIVE")
        String status,
        @Schema(description = "Last value read", example = "78.5")
        Double lastValue,
        @Schema(description = "When it was detected", example = "2026-10-06T12:00:00Z")
        Instant detectedAt) {}
