package com.codenova.vigilia.incidents.infrastructure.persistence.jpa.assemblers;

import com.codenova.vigilia.incidents.domain.model.aggregates.Incident;
import com.codenova.vigilia.incidents.infrastructure.persistence.jpa.entities.IncidentPersistenceEntity;

/**
 * Converts between the Incident aggregate and its persistence entity.
 */
public final class IncidentPersistenceAssembler {

    private IncidentPersistenceAssembler() {
    }

    public static Incident toDomainFromPersistence(IncidentPersistenceEntity entity) {
        return Incident.reconstitute(
                entity.getId(),
                entity.getBuildingId(),
                entity.getResidentId(),
                entity.getDeviceId(),
                entity.getCategory(),
                entity.getDescription(),
                entity.getStatus(),
                entity.getRatingScore(),
                entity.getRatingComment(),
                entity.getReportedAt(),
                entity.getStartedAt(),
                entity.getResolvedAt(),
                entity.getRatedAt());
    }

    public static IncidentPersistenceEntity toPersistenceFromDomain(Incident incident) {
        var entity = new IncidentPersistenceEntity();
        // A new incident has no id yet, so JPA generates it
        if (incident.getId() != null) entity.setId(incident.getId());
        entity.setBuildingId(incident.getBuildingId());
        entity.setResidentId(incident.getResidentId());
        entity.setDeviceId(incident.getDeviceId());
        entity.setCategory(incident.getCategory());
        entity.setDescription(incident.getDescription());
        entity.setStatus(incident.getStatus());
        entity.setRatingScore(incident.getRatingScore());
        entity.setRatingComment(incident.getRatingComment());
        entity.setReportedAt(incident.getReportedAt());
        entity.setStartedAt(incident.getStartedAt());
        entity.setResolvedAt(incident.getResolvedAt());
        entity.setRatedAt(incident.getRatedAt());
        return entity;
    }
}
