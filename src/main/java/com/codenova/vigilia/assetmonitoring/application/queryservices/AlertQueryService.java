package com.codenova.vigilia.assetmonitoring.application.queryservices;

import com.codenova.vigilia.assetmonitoring.domain.model.aggregates.Alert;
import com.codenova.vigilia.assetmonitoring.domain.model.queries.GetAlertByIdQuery;
import com.codenova.vigilia.assetmonitoring.domain.model.queries.GetAlertsByStatusQuery;
import com.codenova.vigilia.assetmonitoring.domain.model.queries.GetAllAlertsQuery;

import java.util.List;
import java.util.Optional;
/**
 * Handles queries that read alerts.
 */
public interface AlertQueryService {
    List<Alert> handle(GetAllAlertsQuery query);

    Optional<Alert> handle(GetAlertByIdQuery query);

    List<Alert> handle(GetAlertsByStatusQuery query);
}
