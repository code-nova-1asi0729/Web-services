package com.codenova.vigilia.incidents.application.internal.commandservices;

import com.codenova.vigilia.incidents.application.commandservices.IncidentCommandFailure;
import com.codenova.vigilia.incidents.application.commandservices.IncidentCommandService;
import com.codenova.vigilia.incidents.domain.model.aggregates.Incident;
import com.codenova.vigilia.incidents.domain.model.commands.RateIncidentCommand;
import com.codenova.vigilia.incidents.domain.model.commands.ReportIncidentCommand;
import com.codenova.vigilia.incidents.domain.model.commands.ResolveIncidentCommand;
import com.codenova.vigilia.incidents.domain.model.commands.StartIncidentManagementCommand;
import com.codenova.vigilia.incidents.domain.repositories.IncidentRepository;
import com.codenova.vigilia.shared.application.patterns.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * Default implementation of IncidentCommandService
 */
@Slf4j
@Service
public class IncidentCommandServiceImpl implements IncidentCommandService {
    private final IncidentRepository incidentRepository;

    public IncidentCommandServiceImpl(IncidentRepository incidentRepository) {
        this.incidentRepository = incidentRepository;
    }

    @Override
    @Transactional
    public Result<Incident, IncidentCommandFailure> handle(ReportIncidentCommand command) {
        var incident = incidentRepository.save(new Incident(command));
        log.info("Incident reported: id={}, buildingId={}", incident.getId(), incident.getBuildingId());
        return Result.success(incident);
    }

    @Override
    @Transactional
    public Result<Incident, IncidentCommandFailure> handle(StartIncidentManagementCommand command) {
        var incident = incidentRepository.findById(command.incidentId());
        if (incident.isEmpty()) return Result.failure(new IncidentCommandFailure.NotFound());
        if (!incident.get().start()) {
            log.warn("Invalid transition: id={}, from={}", command.incidentId(), incident.get().getStatus());
            return Result.failure(new IncidentCommandFailure.InvalidStatusTransition());
        }
        log.info("Incident management started: id={}", command.incidentId());
        return Result.success(incidentRepository.save(incident.get()));
    }

    @Override
    @Transactional
    public Result<Incident, IncidentCommandFailure> handle(ResolveIncidentCommand command) {
        var incident = incidentRepository.findById(command.incidentId());
        if (incident.isEmpty()) return Result.failure(new IncidentCommandFailure.NotFound());
        if (!incident.get().resolve()) {
            log.warn("Invalid transition: id={}, from={}", command.incidentId(), incident.get().getStatus());
            return Result.failure(new IncidentCommandFailure.InvalidStatusTransition());
        }
        log.info("Incident resolved: id={}", command.incidentId());
        return Result.success(incidentRepository.save(incident.get()));
    }

    @Override
    @Transactional
    public Result<Incident, IncidentCommandFailure> handle(RateIncidentCommand command) {
        var incident = incidentRepository.findById(command.incidentId());
        if (incident.isEmpty()) return Result.failure(new IncidentCommandFailure.NotFound());
        if (!incident.get().rate(command.score(), command.comment())) {
            log.warn("Rating not allowed: id={}, status={}", command.incidentId(), incident.get().getStatus());
            return Result.failure(new IncidentCommandFailure.RatingNotAllowed());
        }
        log.info("Incident rated: id={}, score={}", command.incidentId(), command.score().value());
        return Result.success(incidentRepository.save(incident.get()));
    }
}