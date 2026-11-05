package com.codenova.vigilia.incidents.interfaces.rest.transform;

import com.codenova.vigilia.incidents.domain.model.commands.ReportIncidentCommand;
import com.codenova.vigilia.incidents.interfaces.rest.resources.ReportIncidentResource;
/**
 * Builds a ReportIncidentCommand from its REST resource
 */
public class ReportIncidentCommandFromResourceAssembler {
    public static ReportIncidentCommand toCommandFromResource(ReportIncidentResource resource) {
        return new ReportIncidentCommand(
                resource.buildingId(),
                resource.residentId(),
                resource.equipmentId(),
                resource.category(),
                resource.description());
    }
}