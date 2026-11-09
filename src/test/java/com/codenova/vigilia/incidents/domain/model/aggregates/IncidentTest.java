package com.codenova.vigilia.incidents.domain.model.aggregates;

import com.codenova.vigilia.incidents.domain.model.commands.ReportIncidentCommand;
import com.codenova.vigilia.incidents.domain.model.valueobjects.IncidentCategory;
import com.codenova.vigilia.incidents.domain.model.valueobjects.IncidentStatus;
import com.codenova.vigilia.incidents.domain.model.valueobjects.RatingScore;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IncidentTest {

    private Incident newIncident() {
        return new Incident(new ReportIncidentCommand(1L, 3L, null, IncidentCategory.WATER,
                "Baja presión de agua en los pisos altos."));
    }

    @Test
    void goesFromReportedToInProgressToResolved() {
        var incident = newIncident();
        assertEquals(IncidentStatus.REPORTED, incident.getStatus());
        assertTrue(incident.start());
        assertTrue(incident.resolve());
        assertEquals(IncidentStatus.RESOLVED, incident.getStatus());
        assertNotNull(incident.getResolvedAt());
    }

    @Test
    void cannotBeResolvedBeforeItsManagementStarts() {
        var incident = newIncident();
        assertFalse(incident.resolve());
        assertEquals(IncidentStatus.REPORTED, incident.getStatus());
    }

    @Test
    void managementCanStartOnlyOnce() {
        var incident = newIncident();
        incident.start();
        assertFalse(incident.start());
    }

    @Test
    void canOnlyBeRatedOnceAfterResolved() {
        var incident = newIncident();
        assertFalse(incident.rate(new RatingScore(5), null));
        incident.start();
        incident.resolve();
        assertTrue(incident.rate(new RatingScore(5), "Muy rápido."));
        assertFalse(incident.rate(new RatingScore(1), null));
    }
}
