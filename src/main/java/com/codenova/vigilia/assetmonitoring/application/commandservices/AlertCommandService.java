package com.codenova.vigilia.assetmonitoring.application.commandservices;

import com.codenova.vigilia.assetmonitoring.domain.model.aggregates.Alert;
import com.codenova.vigilia.assetmonitoring.domain.model.commands.AcknowledgeAlertCommand;
import com.codenova.vigilia.assetmonitoring.domain.model.commands.RaiseAlertCommand;
import com.codenova.vigilia.assetmonitoring.domain.model.commands.ResolveAlertCommand;
import com.codenova.vigilia.shared.application.patterns.Result;
/**
 * Handles commands that change alerts.
 */
public interface AlertCommandService {

    Result<Alert, AlertCommandFailure> handle(RaiseAlertCommand command);

    Result<Alert, AlertCommandFailure> handle(AcknowledgeAlertCommand command);

    Result<Alert, AlertCommandFailure> handle(ResolveAlertCommand command);
}
