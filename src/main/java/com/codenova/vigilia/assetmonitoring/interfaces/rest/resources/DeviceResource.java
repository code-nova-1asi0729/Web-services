package com.codenova.vigilia.assetmonitoring.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Critical equipment")
public record DeviceResource(
        @Schema(description = "Equipment identifier", example = "1")
        Long id,

        @Schema(description = "Building identifier", example = "1")
        Long buildingId,

        @Schema(description = "Equipment code", example = "LP-WP-01")
        String code,

        @Schema(description = "Equipment name", example = "Bomba hidroneumática 1")
        String name,

        @Schema(description = "Equipment type", example = "WATER_PUMP")
        String type,

        @Schema(description = "Location inside the building", example = "Sótano 1")
        String location,

        @Schema(description = "Installation date", example = "2021-03-15")
        LocalDate installationDate,

        @Schema(description = "Operational status", example = "OPERATIONAL")
        String status) {}