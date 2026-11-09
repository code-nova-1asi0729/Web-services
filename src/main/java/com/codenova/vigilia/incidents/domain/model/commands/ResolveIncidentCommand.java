package com.codenova.vigilia.incidents.domain.model.commands;
/**
 * Command to close an incident that is being managed
 */
public record ResolveIncidentCommand(Long incidentId) {
    public ResolveIncidentCommand {
        if (incidentId == null || incidentId <= 0) throw new IllegalArgumentException("incident.error.id.invalid");
    }
}
