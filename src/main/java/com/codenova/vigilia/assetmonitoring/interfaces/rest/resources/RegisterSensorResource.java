package com.codenova.vigilia.assetmonitoring.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Request to register an IoT sensor")
public record RegisterSensorResource(
        @Schema(description = "Serial number, unique in the platform", example = "VS-1001")
        @NotBlank(message = "{sensor.error.serialNumber.notBlank}")
        @Pattern(regexp = "^[A-Z0-9-]{3,30}$", message = "{sensor.error.serialNumber.pattern}")
        String serialNumber,

        @Schema(description = "Sensor model", example = "VibroSense V2")
        @NotBlank(message = "{sensor.error.model.notBlank}")
        String model) {}
