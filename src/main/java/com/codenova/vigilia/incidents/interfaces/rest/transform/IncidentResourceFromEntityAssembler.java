package com.codenova.vigilia.incidents.interfaces.rest.transform;

import com.codenova.vigilia.incidents.domain.model.aggregates.Incident;
import com.codenova.vigilia.incidents.interfaces.rest.resources.IncidentResource;
/**
 * Builds an IncidentResource from the Incident aggregate
 */
public class IncidentResourceFromEntityAssembler {
    public static IncidentResource toResourceFromEntity(Incident entity) {
        return new IncidentResource(
                entity.getId(),
                entity.getBuildingId(),
                entity.getResidentId(),
                entity.getEquipmentId(),
                entity.getCategory().name(),
                entity.getDescription(),
                entity.getStatus().name(),
                entity.isRated() ? entity.getRatingScore().value() : null,
                entity.getRatingComment(),
                entity.getReportedAt(),
                entity.getStartedAt(),
                entity.getResolvedAt(),
                entity.getRatedAt());
    }
}