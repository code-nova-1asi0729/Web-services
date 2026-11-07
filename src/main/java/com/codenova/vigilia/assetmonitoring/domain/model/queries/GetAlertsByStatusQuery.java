package com.codenova.vigilia.assetmonitoring.domain.model.queries;

import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.AlertStatus;
/**
 * Query to list the alerts in a given status, for example the active ones.
 */
public record GetAlertsByStatusQuery(AlertStatus status) {
    public GetAlertsByStatusQuery {
        if (status == null) throw new IllegalArgumentException("alert.error.status.notNull");
    }
}
