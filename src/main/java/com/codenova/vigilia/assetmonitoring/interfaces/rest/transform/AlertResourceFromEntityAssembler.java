package com.codenova.vigilia.assetmonitoring.interfaces.rest.transform;

import com.codenova.vigilia.assetmonitoring.domain.model.aggregates.Alert;
import com.codenova.vigilia.assetmonitoring.interfaces.rest.resources.AlertResource;
/**
 * Builds an AlertResource from the Alert aggregate.
 */
public class AlertResourceFromEntityAssembler {
    public static AlertResource toResourceFromEntity(Alert entity) {
        return new AlertResource(
                entity.getId(),
                entity.getDeviceId(),
                entity.getMetric().name(),
                entity.getSeverity().name(),
                entity.getStatus().name(),
                entity.getLastValue(),
                entity.getThresholdValue(),
                entity.getDetectedAt());
    }
}
