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
        @Schema(description = "Critical equipment with the out-of-range reading", example = "2")
        @NotNull(message = "{alert.error.equipmentId.invalid}")
        @Positive(message = "{alert.error.equipmentId.invalid}")
        Long equipmentId,

        @Schema(description = "Measured magnitude", example = "TEMPERATURE")
        @NotNull(message = "{alert.error.metric.notNull}")
        MetricType metric,

        @Schema(description = "How serious it is", example = "HIGH")
        @NotNull(message = "{alert.error.severity.notNull}")
        AlertSeverity severity,

        @Schema(description = "Last value read", example = "78.5")
        @NotNull(message = "{alert.error.lastValue.notNull}")
        Double lastValue) {}
