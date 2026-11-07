package com.codenova.vigilia.assetmonitoring.domain.model.commands;
/**
 * Command to acknowledge an active alert, meaning someone is already managing it.
 */
public record AcknowledgeAlertCommand(Long alertId) {
    public AcknowledgeAlertCommand {
        if (alertId == null || alertId <= 0) throw new IllegalArgumentException("alert.error.id.invalid");
    }
}
