package com.codenova.vigilia.incidents.interfaces.rest.transform;

import com.codenova.vigilia.incidents.domain.model.commands.UpdateIncidentCommand;
import com.codenova.vigilia.incidents.domain.model.valueobjects.RatingScore;
import com.codenova.vigilia.incidents.interfaces.rest.resources.UpdateIncidentResource;
/**
 * Builds an UpdateIncidentCommand from its REST resource and the incident id of the route
 */
public class UpdateIncidentCommandFromResourceAssembler {
    public static UpdateIncidentCommand toCommandFromResource(Long incidentId, UpdateIncidentResource resource) {
        var ratingScore = resource.ratingScore() == null ? null : new RatingScore(resource.ratingScore());
        return new UpdateIncidentCommand(incidentId, resource.status(), ratingScore, resource.ratingComment());
    }
}