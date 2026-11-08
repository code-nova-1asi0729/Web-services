package com.codenova.vigilia.assetmonitoring.domain.model.aggregates;

import com.codenova.vigilia.assetmonitoring.domain.model.commands.RegisterSensorCommand;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.SensorStatus;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.SerialNumber;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class SensorTest {

    private Sensor newSensor() {
        return new Sensor(new RegisterSensorCommand(new SerialNumber("VS-1001"), "VibroSense V2"));
    }

    @Test
    void isRegisteredWithoutEquipment() {
        var sensor = newSensor();
        assertEquals(SensorStatus.UNASSIGNED, sensor.getStatus());
        assertFalse(sensor.isAssigned());
    }

    @Test
    void canBeAssignedToOnlyOneEquipmentAtATime() {
        var sensor = newSensor();
        assertTrue(sensor.assignTo(1L));
        assertEquals(SensorStatus.ASSIGNED, sensor.getStatus());
        assertFalse(sensor.assignTo(2L));
        assertEquals(1L, sensor.getEquipmentId());
    }

    @Test
    void canBeReleasedOnlyWhenAssigned() {
        var sensor = newSensor();
        assertFalse(sensor.release());
        sensor.assignTo(1L);
        assertTrue(sensor.release());
        assertNull(sensor.getEquipmentId());
        assertEquals(SensorStatus.UNASSIGNED, sensor.getStatus());
    }

    @Test
    void startsMonitoringWhenItSendsASignal() {
        var sensor = newSensor();
        sensor.assignTo(1L);
        var recordedAt = Instant.parse("2026-11-07T12:00:00Z");
        sensor.registerSignal(recordedAt);
        assertEquals(SensorStatus.MONITORING_ACTIVE, sensor.getStatus());
        assertEquals(recordedAt, sensor.getLastSeenAt());
    }
}
