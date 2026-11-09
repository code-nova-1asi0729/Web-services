package com.codenova.vigilia.assetmonitoring.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "Request to assign a sensor to a critical equipment")
public record AssignSensorResource(
        @Schema(description = "Equipment the sensor will measure", example = "1")
        @NotNull(message = "{sensor.error.equipmentId.invalid}")
        @Positive(message = "{sensor.error.equipmentId.invalid}")
        Long equipmentId) {}
