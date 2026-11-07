package com.codenova.vigilia.assetmonitoring.application.internal.commandservices;

import com.codenova.vigilia.assetmonitoring.application.commandservices.AlertCommandFailure;
import com.codenova.vigilia.assetmonitoring.application.commandservices.AlertCommandService;
import com.codenova.vigilia.assetmonitoring.domain.model.aggregates.Alert;
import com.codenova.vigilia.assetmonitoring.domain.model.commands.AcknowledgeAlertCommand;
import com.codenova.vigilia.assetmonitoring.domain.model.commands.RaiseAlertCommand;
import com.codenova.vigilia.assetmonitoring.domain.model.commands.ResolveAlertCommand;
import com.codenova.vigilia.assetmonitoring.domain.repositories.AlertRepository;
import com.codenova.vigilia.shared.application.patterns.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * Default implementation of AlertCommandService.
 */
@Slf4j
@Service
public class AlertCommandServiceImpl implements AlertCommandService {
    private final AlertRepository alertRepository;

    public AlertCommandServiceImpl(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    @Override
    @Transactional
    public Result<Alert, AlertCommandFailure> handle(RaiseAlertCommand command) {
        var alert = alertRepository.save(new Alert(command));
        log.info("Alert raised: id={}, equipmentId={}, severity={}", alert.getId(), alert.getEquipmentId(), alert.getSeverity());
        return Result.success(alert);
    }

    @Override
    @Transactional
    public Result<Alert, AlertCommandFailure> handle(AcknowledgeAlertCommand command) {
        var result = alertRepository.findById(command.alertId());
        if (result.isEmpty()) {
            log.warn("Alert not found: id={}", command.alertId());
            return Result.failure(new AlertCommandFailure.NotFound());
        }
        var alert = result.get();
        if (!alert.acknowledge()) {
            log.warn("Alert cannot be acknowledged: id={}, status={}", alert.getId(), alert.getStatus());
            return Result.failure(new AlertCommandFailure.InvalidStatusTransition());
        }
        var acknowledged = alertRepository.save(alert);
        log.info("Alert acknowledged: id={}", acknowledged.getId());
        return Result.success(acknowledged);
    }

    @Override
    @Transactional
    public Result<Alert, AlertCommandFailure> handle(ResolveAlertCommand command) {
        var result = alertRepository.findById(command.alertId());
        if (result.isEmpty()) {
            log.warn("Alert not found: id={}", command.alertId());
            return Result.failure(new AlertCommandFailure.NotFound());
        }
        var alert = result.get();
        if (!alert.resolve()) {
            log.warn("Alert cannot be resolved: id={}, status={}", alert.getId(), alert.getStatus());
            return Result.failure(new AlertCommandFailure.InvalidStatusTransition());
        }
        var resolved = alertRepository.save(alert);
        log.info("Alert resolved: id={}", resolved.getId());
        return Result.success(resolved);
    }
}
