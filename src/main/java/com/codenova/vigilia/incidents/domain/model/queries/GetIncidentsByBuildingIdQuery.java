package com.codenova.vigilia.incidents.domain.model.queries;
/**
 * Query to list the incidents of a building
 */
public record GetIncidentsByBuildingIdQuery(Long buildingId) {
    public GetIncidentsByBuildingIdQuery {
        if (buildingId == null || buildingId <= 0) throw new IllegalArgumentException("incident.error.buildingId.invalid");
    }
}