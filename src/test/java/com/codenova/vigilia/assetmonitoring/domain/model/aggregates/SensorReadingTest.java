package com.codenova.vigilia.assetmonitoring.domain.model.aggregates;

import com.codenova.vigilia.assetmonitoring.domain.model.commands.RecordReadingCommand;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.MetricType;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.ReadingStatus;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class SensorReadingTest {

    private static final Instant RECORDED_AT = Instant.parse("2026-11-07T12:00:00Z");

    @Test
    void takesTheEquipmentFromTheSensorAssignment() {
        var reading = new SensorReading(new RecordReadingCommand(1L, MetricType.VIBRATION, 2.3, "mm/s", RECORDED_AT), 5L);
        assertEquals(5L, reading.getEquipmentId());
        assertEquals(ReadingStatus.VALID, reading.getStatus());
    }

    @Test
    void isInvalidWhenTheValueIsPhysicallyImpossible() {
        var humidity = new SensorReading(new RecordReadingCommand(1L, MetricType.HUMIDITY, 120.0, "%", RECORDED_AT), 5L);
        var vibration = new SensorReading(new RecordReadingCommand(1L, MetricType.VIBRATION, -1.0, "mm/s", RECORDED_AT), 5L);
        assertFalse(humidity.isValid());
        assertFalse(vibration.isValid());
    }

    @Test
    void cannotBeRecordedInTheFuture() {
        var tomorrow = Instant.now().plusSeconds(86_400);
        assertThrows(IllegalArgumentException.class,
                () -> new RecordReadingCommand(1L, MetricType.TEMPERATURE, 25.0, "°C", tomorrow));
    }
}
