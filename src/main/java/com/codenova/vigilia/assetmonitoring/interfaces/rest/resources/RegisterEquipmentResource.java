package com.codenova.vigilia.assetmonitoring.interfaces.rest.resources;

import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.EquipmentType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

@Schema(description = "Request to create a critical equipment")
public record RegisterEquipmentResource(
        @Schema(description = "Building identifier", example = "1")
        @NotNull(message = "{equipment.error.buildingId.invalid}")
        @Positive(message = "{equipment.error.buildingId.invalid}")
        Long buildingId,

        @Schema(description = "Equipment code, unique inside the building", example = "LP-WP-01")
        @NotBlank(message = "{equipment.error.code.notBlank}")
        @Pattern(regexp = "^[A-Z0-9-]{2,30}$", message = "{equipment.error.code.pattern}")
        String code,

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
        LocalDate installationDate) {}