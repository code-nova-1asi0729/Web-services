package com.codenova.vigilia.incidents.interfaces.rest.transform;

import com.codenova.vigilia.incidents.domain.model.commands.RateIncidentCommand;
import com.codenova.vigilia.incidents.domain.model.valueobjects.RatingScore;
import com.codenova.vigilia.incidents.interfaces.rest.resources.RateIncidentResource;
/**
 * Builds a RateIncidentCommand from its REST resource and the incident id of the route
 */
public class RateIncidentCommandFromResourceAssembler {
    public static RateIncidentCommand toCommandFromResource(Long incidentId, RateIncidentResource resource) {
        return new RateIncidentCommand(incidentId, new RatingScore(resource.score()), resource.comment());
    }
}
