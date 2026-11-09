package com.codenova.vigilia.assetmonitoring.interfaces.rest.resources;

import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.MetricType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.time.Instant;

@Schema(description = "Request sent by a sensor to record a reading")
public record RecordReadingResource(
        @Schema(description = "Sensor that sends the reading", example = "1")
        @NotNull(message = "{reading.error.sensorId.invalid}")
        @Positive(message = "{reading.error.sensorId.invalid}")
        Long sensorId,

        @Schema(description = "Measured magnitude", example = "VIBRATION")
        @NotNull(message = "{reading.error.metric.notNull}")
        MetricType metric,

        @Schema(description = "Measured value", example = "2.3")
        @NotNull(message = "{reading.error.value.notNull}")
        Double value,

        @Schema(description = "Unit of the value", example = "mm/s")
        @NotBlank(message = "{reading.error.unit.notBlank}")
        String unit,

        @Schema(description = "When the sensor measured the value", example = "2026-11-07T12:00:00Z")
        @NotNull(message = "{reading.error.recordedAt.invalid}")
        @PastOrPresent(message = "{reading.error.recordedAt.invalid}")
        Instant recordedAt) {}
