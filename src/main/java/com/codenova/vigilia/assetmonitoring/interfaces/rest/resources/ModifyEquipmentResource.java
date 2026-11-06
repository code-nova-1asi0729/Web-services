package com.codenova.vigilia.assetmonitoring.interfaces.rest.resources;

import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.EquipmentStatus;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.EquipmentType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

@Schema(description = "Request to update a critical equipment")
public record ModifyEquipmentResource(
        @Schema(description = "Equipment name", example = "Bomba hidroneumática 1")
        @NotBlank(message = "{equipment.error.name.notBlank}")
        String name,

        @Schema(description = "Equipment type", example = "WATER_PUMP")
        @NotNull(message = "{equipment.error.type.notNull}")
        EquipmentType type,

        @Schema(description = "Location inside the building", example = "Sótano 1")
        @NotBlank(message = "{equipment.error.location.notBlank}")
        String location,

        @Schema(description = "Installation date", example = "2021-03-15")
        @NotNull(message = "{equipment.error.installationDate.invalid}")
        @PastOrPresent(message = "{equipment.error.installationDate.invalid}")
        LocalDate installationDate,

        @Schema(description = "Operational status", example = "OPERATIONAL")
        @NotNull(message = "{equipment.error.status.notNull}")
        EquipmentStatus status) {}