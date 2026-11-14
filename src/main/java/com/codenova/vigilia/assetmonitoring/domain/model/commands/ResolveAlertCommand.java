package com.codenova.vigilia.assetmonitoring.domain.model.commands;
/**
 * Command to resolve an alert once the device is back to normal.
 */
public record ResolveAlertCommand(Long alertId) {
    public ResolveAlertCommand {
        if (alertId == null || alertId <= 0) throw new IllegalArgumentException("alert.error.id.invalid");
    }
}
