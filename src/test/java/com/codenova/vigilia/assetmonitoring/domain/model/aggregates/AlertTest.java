package com.codenova.vigilia.assetmonitoring.domain.model.aggregates;

import com.codenova.vigilia.assetmonitoring.domain.model.commands.RaiseAlertCommand;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.AlertSeverity;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.AlertStatus;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.MetricType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AlertTest {

    private Alert newAlert() {
        return new Alert(new RaiseAlertCommand(2L, MetricType.TEMPERATURE, AlertSeverity.HIGH, 78.5));
    }

    @Test
    void isRaisedActiveAndCanBeAcknowledgedThenResolved() {
        var alert = newAlert();
        assertEquals(AlertStatus.ACTIVE, alert.getStatus());
        assertTrue(alert.acknowledge());
        assertTrue(alert.resolve());
        assertEquals(AlertStatus.RESOLVED, alert.getStatus());
        assertNotNull(alert.getResolvedAt());
    }

    @Test
    void activeAlertCanBeResolvedDirectly() {
        var alert = newAlert();
        assertTrue(alert.resolve());
    }

    @Test
    void resolvedAlertCannotBeAcknowledgedOrResolvedAgain() {
        var alert = newAlert();
        alert.resolve();
        assertFalse(alert.acknowledge());
        assertFalse(alert.resolve());
        assertEquals(AlertStatus.RESOLVED, alert.getStatus());
    }

    @Test
    void acknowledgedAlertCannotBeAcknowledgedAgain() {
        var alert = newAlert();
        alert.acknowledge();
        assertFalse(alert.acknowledge());
    }
}
