package com.codenova.vigilia.assetmonitoring.interfaces.rest.resources;

import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.DeviceStatus;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.DeviceType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

@Schema(description = "Request to update a critical device")
public record ModifyDeviceResource(
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
        LocalDate installationDate,

        @Schema(description = "Operational status", example = "OPERATIONAL")
        @NotNull(message = "{device.error.status.notNull}")
        DeviceStatus status) {}