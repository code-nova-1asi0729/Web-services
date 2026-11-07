package com.codenova.vigilia.assetmonitoring.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
/**
 * Request body to register a building.
 */
@Schema(description = "Request to register a building")
public record RegisterBuildingResource(
        @Schema(description = "Building name", example = "Residencial Los Pinos")
        @NotBlank(message = "{building.error.name.notBlank}")
        String name,

        @Schema(description = "Unique building code", example = "LP-01")
        @NotBlank(message = "{building.error.code.notBlank}")
        @Pattern(regexp = "^[A-Z0-9-]{2,20}$", message = "{building.error.code.pattern}")
        String code,

        @Schema(description = "Street address", example = "Av. Los Pinos 450")
        @NotBlank(message = "{building.error.address.notBlank}")
        String address,

        @Schema(description = "District", example = "Santiago de Surco")
        @NotBlank(message = "{building.error.district.notBlank}")
        String district,

        @Schema(description = "Number of residential units", example = "48")
        @NotNull(message = "{building.error.totalUnits.positive}")
        @Positive(message = "{building.error.totalUnits.positive}")
        Integer totalUnits) {}