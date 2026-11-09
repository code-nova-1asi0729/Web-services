package com.codenova.vigilia.incidents.domain.model.commands;

import com.codenova.vigilia.incidents.domain.model.valueobjects.RatingScore;
/**
 * Command for the resident to rate how a resolved incident was handled
 */
public record RateIncidentCommand(Long incidentId, RatingScore score, String comment) {
    public RateIncidentCommand {
        if (incidentId == null || incidentId <= 0) throw new IllegalArgumentException("incident.error.id.invalid");
        if (score == null) throw new IllegalArgumentException("incident.error.rating.range");
        if (comment != null && comment.length() > 300) throw new IllegalArgumentException("incident.error.ratingComment.size");
    }
}
