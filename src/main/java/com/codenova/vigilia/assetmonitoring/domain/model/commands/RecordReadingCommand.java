package com.codenova.vigilia.assetmonitoring.domain.model.commands;

import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.MetricType;

import java.time.Instant;

/**
 * Command sent by a sensor (or the simulator) with one measured value.
 */
public record RecordReadingCommand(Long sensorId, MetricType metric, Double value, String unit, Instant recordedAt) {

    public RecordReadingCommand {
        if (sensorId == null || sensorId <= 0) throw new IllegalArgumentException("reading.error.sensorId.invalid");
        if (metric == null) throw new IllegalArgumentException("reading.error.metric.notNull");
        if (value == null) throw new IllegalArgumentException("reading.error.value.notNull");
        if (unit == null || unit.isBlank()) throw new IllegalArgumentException("reading.error.unit.notBlank");
        if (recordedAt == null || recordedAt.isAfter(Instant.now())) throw new IllegalArgumentException("reading.error.recordedAt.invalid");
    }
}
