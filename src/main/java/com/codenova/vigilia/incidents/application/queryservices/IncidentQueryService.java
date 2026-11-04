package com.codenova.vigilia.incidents.application.queryservices;

import com.codenova.vigilia.incidents.domain.model.aggregates.Incident;
import com.codenova.vigilia.incidents.domain.model.queries.GetAllIncidentsQuery;
import com.codenova.vigilia.incidents.domain.model.queries.GetIncidentByIdQuery;
import com.codenova.vigilia.incidents.domain.model.queries.GetIncidentsByBuildingIdQuery;
import java.util.List;
import java.util.Optional;
/**
 * Handles queries that read incidents
 */
public interface IncidentQueryService {
    List<Incident> handle(GetAllIncidentsQuery query);

    Optional<Incident> handle(GetIncidentByIdQuery query);

    List<Incident> handle(GetIncidentsByBuildingIdQuery query);
}