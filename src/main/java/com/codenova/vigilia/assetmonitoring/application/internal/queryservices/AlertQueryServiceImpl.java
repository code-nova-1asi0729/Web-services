package com.codenova.vigilia.assetmonitoring.application.internal.queryservices;

import com.codenova.vigilia.assetmonitoring.application.queryservices.AlertQueryService;
import com.codenova.vigilia.assetmonitoring.domain.model.aggregates.Alert;
import com.codenova.vigilia.assetmonitoring.domain.model.queries.GetAlertByIdQuery;
import com.codenova.vigilia.assetmonitoring.domain.model.queries.GetAlertsByStatusQuery;
import com.codenova.vigilia.assetmonitoring.domain.model.queries.GetAllAlertsQuery;
import com.codenova.vigilia.assetmonitoring.domain.repositories.AlertRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
/**
 * Default implementation of AlertQueryService.
 */
@Service
public class AlertQueryServiceImpl implements AlertQueryService {
    private final AlertRepository alertRepository;

    public AlertQueryServiceImpl(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    @Override
    public List<Alert> handle(GetAllAlertsQuery query) {
        return alertRepository.findAll();
    }

    @Override
    public Optional<Alert> handle(GetAlertByIdQuery query) {
        return alertRepository.findById(query.alertId());
    }

    @Override
    public List<Alert> handle(GetAlertsByStatusQuery query) {
        return alertRepository.findAllByStatus(query.status());
    }
}
