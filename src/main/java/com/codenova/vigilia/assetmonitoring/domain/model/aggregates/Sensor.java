package com.codenova.vigilia.assetmonitoring.domain.model.aggregates;

import com.codenova.vigilia.assetmonitoring.domain.model.commands.RegisterSensorCommand;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.SensorStatus;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.SerialNumber;
import com.codenova.vigilia.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Getter;

import java.time.Instant;

/**
 * IoT sensor that measures one critical equipment at a time.
 */
@Getter
public class Sensor extends AbstractDomainAggregateRoot<Sensor> {

    private Long id;
    private Long equipmentId;
    private SerialNumber serialNumber;
    private String model;
    private SensorStatus status;
    private Instant lastSeenAt;

    private Sensor() {}

    public Sensor(RegisterSensorCommand command) {
        this.serialNumber = command.serialNumber();
        this.model = command.model();
        this.status = SensorStatus.UNASSIGNED;
    }

    public static Sensor reconstitute(Long id, Long equipmentId, SerialNumber serialNumber, String model,
                                      SensorStatus status, Instant lastSeenAt) {
        var sensor = new Sensor();
        sensor.id = id;
        sensor.equipmentId = equipmentId;
        sensor.serialNumber = serialNumber;
        sensor.model = model;
        sensor.status = status;
        sensor.lastSeenAt = lastSeenAt;
        return sensor;
    }

    public boolean isAssigned() {
        return this.equipmentId != null;
    }

    /**
     * Links the sensor to an equipment. Returns false if it already measures another equipment.
     */
    public boolean assignTo(Long equipmentId) {
        if (isAssigned() || this.status == SensorStatus.INACTIVE) return false;
        this.equipmentId = equipmentId;
        this.status = SensorStatus.ASSIGNED;
        return true;
    }

    /**
     * Releases the sensor from its equipment. Returns false if it was not assigned.
     */
    public boolean release() {
        if (!isAssigned()) return false;
        this.equipmentId = null;
        this.status = SensorStatus.UNASSIGNED;
        return true;
    }

    /**
     * Registers that the sensor sent a reading, so it is actively monitoring its equipment.
     */
    public void registerSignal(Instant recordedAt) {
        if (this.lastSeenAt == null || recordedAt.isAfter(this.lastSeenAt)) this.lastSeenAt = recordedAt;
        this.status = SensorStatus.MONITORING_ACTIVE;
    }
}
