package com.codenova.vigilia.assetmonitoring.interfaces.rest.resources;

import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.DeviceType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

@Schema(description = "Request to create a device")
public record RegisterDeviceResource(
        @Schema(description = "Building identifier", example = "1")
        @NotNull(message = "{device.error.buildingId.invalid}")
        @Positive(message = "{device.error.buildingId.invalid}")
        Long buildingId,

        @Schema(description = "Device code, unique inside the building", example = "LP-WP-01")
        @NotBlank(message = "{device.error.code.notBlank}")
        @Pattern(regexp = "^[A-Z0-9-]{2,30}$", message = "{device.error.code.pattern}")
        String code,

        @Schema(description = "Device name", example = "Bomba hidroneumática 1")
        @NotBlank(message = "{device.error.name.notBlank}")
        String name,

        @Schema(description = "Device type", example = "WATER_PUMP")
        @NotNull(message = "{device.error.type.notNull}")
        DeviceType type,

        @Schema(description = "Location inside the building", example = "Sótano 1")
        @NotBlank(message = "{device.error.location.notBlank}")
        String location,

        @Schema(description = "Installation date", example = "2021-03-15")
        @NotNull(message = "{device.error.installationDate.invalid}")
        @PastOrPresent(message = "{device.error.installationDate.invalid}")
        LocalDate installationDate) {}