package com.codenova.vigilia.assetmonitoring.interfaces.rest.resources;

import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.AlertSeverity;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.MetricType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
/**
 * Request body to raise an alert.
 */
@Schema(description = "Request to raise an alert")
public record RaiseAlertResource(
        @Schema(description = "Device with the out-of-range reading", example = "2")
        @NotNull(message = "{alert.error.deviceId.invalid}")
        @Positive(message = "{alert.error.deviceId.invalid}")
        Long deviceId,

        @Schema(description = "Measured magnitude", example = "TEMPERATURE")
        @NotNull(message = "{alert.error.metric.notNull}")
        MetricType metric,

        @Schema(description = "How serious it is", example = "HIGH")
        @NotNull(message = "{alert.error.severity.notNull}")
        AlertSeverity severity,

        @Schema(description = "Last value read", example = "78.5")
        @NotNull(message = "{alert.error.lastValue.notNull}")
        Double lastValue,

        @Schema(description = "Normal limit for this metric", example = "4.5")
        @NotNull(message = "{alert.error.thresholdValue.invalid}")
        @Positive(message = "{alert.error.thresholdValue.invalid}")
        Double thresholdValue) {}
