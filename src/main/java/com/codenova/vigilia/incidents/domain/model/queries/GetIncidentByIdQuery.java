package com.codenova.vigilia.incidents.domain.model.queries;
/**
 * Query to find an incident by its identifier
 */
public record GetIncidentByIdQuery(Long incidentId) {
    public GetIncidentByIdQuery {
        if (incidentId == null || incidentId <= 0) throw new IllegalArgumentException("incident.error.id.invalid");
    }
}