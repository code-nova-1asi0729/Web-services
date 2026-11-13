package com.codenova.vigilia.assetmonitoring.infrastructure.persistence.jpa.assemblers;

import com.codenova.vigilia.assetmonitoring.domain.model.aggregates.Alert;
import com.codenova.vigilia.assetmonitoring.infrastructure.persistence.jpa.entities.AlertPersistenceEntity;

/**
 * Converts between the Alert aggregate and its persistence entity.
 */
public final class AlertPersistenceAssembler {

    private AlertPersistenceAssembler() {
    }

    public static Alert toDomainFromPersistence(AlertPersistenceEntity entity) {
        return Alert.reconstitute(
                entity.getId(),
                entity.getDeviceId(),
                entity.getMetric(),
                entity.getSeverity(),
                entity.getStatus(),
                entity.getLastValue(),
                entity.getDetectedAt(),
                entity.getResolvedAt());
    }

    public static AlertPersistenceEntity toPersistenceFromDomain(Alert alert) {
        var entity = new AlertPersistenceEntity();
        // A new alert has no id yet, so JPA generates it
        if (alert.getId() != null) entity.setId(alert.getId());
        entity.setDeviceId(alert.getDeviceId());
        entity.setMetric(alert.getMetric());
        entity.setSeverity(alert.getSeverity());
        entity.setStatus(alert.getStatus());
        entity.setLastValue(alert.getLastValue());
        entity.setDetectedAt(alert.getDetectedAt());
        entity.setResolvedAt(alert.getResolvedAt());
        return entity;
    }
}
