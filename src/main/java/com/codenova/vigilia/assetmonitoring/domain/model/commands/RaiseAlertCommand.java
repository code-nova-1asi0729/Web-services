package com.codenova.vigilia.assetmonitoring.domain.model.commands;

import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.AlertSeverity;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.MetricType;
/**
 * Command to raise an alert for a critical equipment whose reading is out of range.
 */
public record RaiseAlertCommand(Long equipmentId, MetricType metric, AlertSeverity severity, Double lastValue) {
    public RaiseAlertCommand {
        if (equipmentId == null || equipmentId <= 0) throw new IllegalArgumentException("alert.error.equipmentId.invalid");
        if (metric == null) throw new IllegalArgumentException("alert.error.metric.notNull");
        if (severity == null) throw new IllegalArgumentException("alert.error.severity.notNull");
        if (lastValue == null) throw new IllegalArgumentException("alert.error.lastValue.notNull");
    }
}
