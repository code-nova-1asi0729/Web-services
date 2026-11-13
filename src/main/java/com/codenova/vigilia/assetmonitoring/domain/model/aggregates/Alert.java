package com.codenova.vigilia.assetmonitoring.domain.model.aggregates;

import com.codenova.vigilia.assetmonitoring.domain.model.commands.RaiseAlertCommand;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.AlertSeverity;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.AlertStatus;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.MetricType;
import com.codenova.vigilia.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Getter;

import java.time.Instant;
/**
 * Early warning raised when a reading of a device is out of range.
 */
@Getter
public class Alert extends AbstractDomainAggregateRoot<Alert> {

    private Long id;
    private Long deviceId;
    private MetricType metric;
    private AlertSeverity severity;
    private AlertStatus status;
    private Double lastValue;
    private Double thresholdValue;
    private Instant detectedAt;
    private Instant resolvedAt;

    private Alert() {}

    public Alert(RaiseAlertCommand command) {
        this.deviceId = command.deviceId();
        this.metric = command.metric();
        this.severity = command.severity();
        this.lastValue = command.lastValue();
        this.thresholdValue = command.thresholdValue();
        this.status = AlertStatus.ACTIVE;
        this.detectedAt = Instant.now();
    }

    public static Alert reconstitute(Long id, Long deviceId, MetricType metric, AlertSeverity severity,
                                     AlertStatus status, Double lastValue, Double thresholdValue,
                                     Instant detectedAt, Instant resolvedAt) {
        var alert = new Alert();
        alert.id = id;
        alert.deviceId = deviceId;
        alert.metric = metric;
        alert.severity = severity;
        alert.status = status;
        alert.lastValue = lastValue;
        alert.thresholdValue = thresholdValue;
        alert.detectedAt = detectedAt;
        alert.resolvedAt = resolvedAt;
        return alert;
    }

    public boolean acknowledge() {
        if (status != AlertStatus.ACTIVE) return false;
        this.status = AlertStatus.ACKNOWLEDGED;
        return true;
    }

    public boolean resolve() {
        if (status == AlertStatus.RESOLVED) return false;
        this.status = AlertStatus.RESOLVED;
        this.resolvedAt = Instant.now();
        return true;
    }

    /**
     * How many times the last value is above its threshold. Used to decide which alert to attend first.
     */
    public double exceedRatio() {
        return lastValue / thresholdValue;
    }
}
