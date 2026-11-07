package com.codenova.vigilia.assetmonitoring.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
/**
 * Response body that represents a building.
 */
@Schema(description = "Building")
public record BuildingResource(
        @Schema(description = "Building identifier", example = "1")
        Long id,
        @Schema(description = "Building name", example = "Residencial Los Pinos")
        String name,
        @Schema(description = "Unique building code", example = "LP-01")
        String code,
        @Schema(description = "Street address", example = "Av. Los Pinos 450")
        String address,
        @Schema(description = "District", example = "Santiago de Surco")
        String district,
        @Schema(description = "Number of residential units", example = "48")
        Integer totalUnits,
        @Schema(description = "Operational status", example = "ACTIVE")
        String status) {}