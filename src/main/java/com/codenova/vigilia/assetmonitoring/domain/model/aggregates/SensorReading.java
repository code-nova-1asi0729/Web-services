package com.codenova.vigilia.assetmonitoring.domain.model.aggregates;

import com.codenova.vigilia.assetmonitoring.domain.model.commands.RecordReadingCommand;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.MetricType;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.ReadingStatus;
import com.codenova.vigilia.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Getter;

import java.time.Instant;

/**
 * Value measured by a sensor on a critical equipment at a given moment.
 */
@Getter
public class SensorReading extends AbstractDomainAggregateRoot<SensorReading> {

    private Long id;
    private Long sensorId;
    private Long equipmentId;
    private MetricType metric;
    private Double value;
    private String unit;
    private ReadingStatus status;
    private Instant recordedAt;

    private SensorReading() {}

    /**
     * Records a reading of a sensor. The equipment comes from the sensor assignment.
     */
    public SensorReading(RecordReadingCommand command, Long equipmentId) {
        this.sensorId = command.sensorId();
        this.equipmentId = equipmentId;
        this.metric = command.metric();
        this.value = command.value();
        this.unit = command.unit();
        this.recordedAt = command.recordedAt();
        this.status = isPhysicallyPossible(command.metric(), command.value()) ? ReadingStatus.VALID : ReadingStatus.INVALID;
    }

    public static SensorReading reconstitute(Long id, Long sensorId, Long equipmentId, MetricType metric, Double value,
                                             String unit, ReadingStatus status, Instant recordedAt) {
        var reading = new SensorReading();
        reading.id = id;
        reading.sensorId = sensorId;
        reading.equipmentId = equipmentId;
        reading.metric = metric;
        reading.value = value;
        reading.unit = unit;
        reading.status = status;
        reading.recordedAt = recordedAt;
        return reading;
    }

    public boolean isValid() {
        return this.status == ReadingStatus.VALID;
    }

    private static boolean isPhysicallyPossible(MetricType metric, double value) {
        return switch (metric) {
            case TEMPERATURE -> value >= -40 && value <= 150;
            case HUMIDITY -> value >= 0 && value <= 100;
            case VIBRATION, POWER_CONSUMPTION -> value >= 0;
        };
    }
}
