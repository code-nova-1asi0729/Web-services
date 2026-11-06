package com.codenova.vigilia.incidents.application.internal.queryservices;

import com.codenova.vigilia.incidents.application.queryservices.IncidentQueryService;
import com.codenova.vigilia.incidents.domain.model.aggregates.Incident;
import com.codenova.vigilia.incidents.domain.model.queries.GetAllIncidentsQuery;
import com.codenova.vigilia.incidents.domain.model.queries.GetIncidentByIdQuery;
import com.codenova.vigilia.incidents.domain.model.queries.GetIncidentsByBuildingIdQuery;
import com.codenova.vigilia.incidents.domain.repositories.IncidentRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
/**
 * Default implementation of IncidentQueryService
 */
@Service
public class IncidentQueryServiceImpl implements IncidentQueryService {
    private final IncidentRepository incidentRepository;

    public IncidentQueryServiceImpl(IncidentRepository incidentRepository) {
        this.incidentRepository = incidentRepository;
    }

    @Override
    public List<Incident> handle(GetAllIncidentsQuery query) {
        return incidentRepository.findAll();
    }

    @Override
    public Optional<Incident> handle(GetIncidentByIdQuery query) {
        return incidentRepository.findById(query.incidentId());
    }

    @Override
    public List<Incident> handle(GetIncidentsByBuildingIdQuery query) {
        return incidentRepository.findAllByBuildingId(query.buildingId());
    }
}