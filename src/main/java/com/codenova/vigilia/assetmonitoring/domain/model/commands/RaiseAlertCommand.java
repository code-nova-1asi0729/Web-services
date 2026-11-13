package com.codenova.vigilia.assetmonitoring.domain.model.commands;

import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.AlertSeverity;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.MetricType;
/**
 * Command to raise an alert for a device whose reading is out of range.
 */
public record RaiseAlertCommand(Long deviceId, MetricType metric, AlertSeverity severity, Double lastValue) {
    public RaiseAlertCommand {
        if (deviceId == null || deviceId <= 0) throw new IllegalArgumentException("alert.error.deviceId.invalid");
        if (metric == null) throw new IllegalArgumentException("alert.error.metric.notNull");
        if (severity == null) throw new IllegalArgumentException("alert.error.severity.notNull");
        if (lastValue == null) throw new IllegalArgumentException("alert.error.lastValue.notNull");
    }
}
