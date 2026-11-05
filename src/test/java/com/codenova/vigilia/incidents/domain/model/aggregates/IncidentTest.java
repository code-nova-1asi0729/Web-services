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
    void startsAsReportedAndCanBeResolved() {
        var incident = newIncident();
        assertEquals(IncidentStatus.REPORTED, incident.getStatus());
        assertTrue(incident.changeStatus(IncidentStatus.IN_PROGRESS));
        assertTrue(incident.changeStatus(IncidentStatus.RESOLVED));
        assertNotNull(incident.getResolvedAt());
    }

    @Test
    void cannotGoBackToReported() {
        var incident = newIncident();
        incident.start();
        assertFalse(incident.changeStatus(IncidentStatus.REPORTED));
        assertEquals(IncidentStatus.IN_PROGRESS, incident.getStatus());
    }

    @Test
    void canOnlyBeRatedOnceAfterResolved() {
        var incident = newIncident();
        assertFalse(incident.rate(new RatingScore(5), null));
        incident.resolve();
        assertTrue(incident.rate(new RatingScore(5), "Muy rápido."));
        assertFalse(incident.rate(new RatingScore(1), null));
    }
}