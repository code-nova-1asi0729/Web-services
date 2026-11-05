package com.codenova.vigilia.incidents.domain.model.commands;

import com.codenova.vigilia.incidents.domain.model.valueobjects.IncidentCategory;
/**
 * Command to report a new incident, equipmentId is optional
 */
public record ReportIncidentCommand(Long buildingId, Long residentId, Long equipmentId,
                                    IncidentCategory category, String description) {
    public ReportIncidentCommand {
        if (buildingId == null || buildingId <= 0) throw new IllegalArgumentException("incident.error.buildingId.invalid");
        if (residentId == null || residentId <= 0) throw new IllegalArgumentException("incident.error.residentId.invalid");
        if (equipmentId != null && equipmentId <= 0) throw new IllegalArgumentException("incident.error.equipmentId.invalid");
        if (category == null) throw new IllegalArgumentException("incident.error.category.notNull");
        if (description == null || description.isBlank() || description.length() < 10 || description.length() > 500)
            throw new IllegalArgumentException("incident.error.description.size");
    }
}