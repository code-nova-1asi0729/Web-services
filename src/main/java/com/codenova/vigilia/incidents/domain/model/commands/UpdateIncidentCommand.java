package com.codenova.vigilia.incidents.domain.model.commands;

import com.codenova.vigilia.incidents.domain.model.valueobjects.IncidentStatus;
import com.codenova.vigilia.incidents.domain.model.valueobjects.RatingScore;
/**
 * Command to change the status of an incident and, optionally, rate it
 */
public record UpdateIncidentCommand(Long incidentId, IncidentStatus status,
                                    RatingScore ratingScore, String ratingComment) {
    public UpdateIncidentCommand {
        if (incidentId == null || incidentId <= 0) throw new IllegalArgumentException("incident.error.id.invalid");
        if (status == null) throw new IllegalArgumentException("incident.error.status.notNull");
        if (ratingComment != null && ratingComment.length() > 300)
            throw new IllegalArgumentException("incident.error.ratingComment.size");
    }
}