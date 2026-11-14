package com.codenova.vigilia.assetmonitoring.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Device of a building: water pump, electrical panel, elevator or HVAC")
public record DeviceResource(
        @Schema(description = "Device identifier", example = "1")
        Long id,

        @Schema(description = "Building identifier", example = "1")
        Long buildingId,

        @Schema(description = "Device code, unique in its building", example = "LP-WP-01")
        String code,

        @Schema(description = "Device name", example = "Bomba hidroneumática 1")
        String name,

        @Schema(description = "Device type", example = "WATER_PUMP")
        String type,

        @Schema(description = "Location inside the building", example = "Sótano 1")
        String location,

        @Schema(description = "Installation date", example = "2021-03-15")
        LocalDate installationDate,

        @Schema(description = "Operational status", example = "OPERATIONAL")
        String status) {}