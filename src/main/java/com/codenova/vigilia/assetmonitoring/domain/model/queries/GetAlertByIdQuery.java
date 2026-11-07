package com.codenova.vigilia.assetmonitoring.domain.model.queries;
/**
 * Query to get an alert by its identifier.
 */
public record GetAlertByIdQuery(Long alertId) {
    public GetAlertByIdQuery {
        if (alertId == null || alertId <= 0) throw new IllegalArgumentException("alert.error.id.invalid");
    }
}
