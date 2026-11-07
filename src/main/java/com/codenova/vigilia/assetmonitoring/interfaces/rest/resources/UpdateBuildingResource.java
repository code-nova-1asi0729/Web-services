package com.codenova.vigilia.assetmonitoring.interfaces.rest.resources;

import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.BuildingStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
/**
 * Request body to update a building.
 */
@Schema(description = "Request to update a building")
public record UpdateBuildingResource(
        @Schema(description = "Building name", example = "Condominio Mar Azul")
        @NotBlank(message = "{building.error.name.notBlank}")
        String name,

        @Schema(description = "Street address", example = "Jr. Las Gaviotas 120")
        @NotBlank(message = "{building.error.address.notBlank}")
        String address,

        @Schema(description = "District", example = "Chorrillos")
        @NotBlank(message = "{building.error.district.notBlank}")
        String district,

        @Schema(description = "Number of residential units", example = "64")
        @NotNull(message = "{building.error.totalUnits.positive}")
        @Positive(message = "{building.error.totalUnits.positive}")
        Integer totalUnits,

        @Schema(description = "Operational status", example = "INACTIVE")
        @NotNull(message = "{building.error.status.notNull}")
        BuildingStatus status) {}