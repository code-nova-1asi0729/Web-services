package com.codenova.vigilia.incidents.domain.model.commands;
/**
 * Command to start managing a reported incident
 */
public record StartIncidentManagementCommand(Long incidentId) {
    public StartIncidentManagementCommand {
        if (incidentId == null || incidentId <= 0) throw new IllegalArgumentException("incident.error.id.invalid");
    }
}
