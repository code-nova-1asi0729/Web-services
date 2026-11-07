package com.codenova.vigilia.incidents.application.internal.commandservices;

import com.codenova.vigilia.incidents.application.commandservices.IncidentCommandFailure;
import com.codenova.vigilia.incidents.application.commandservices.IncidentCommandService;
import com.codenova.vigilia.incidents.domain.model.aggregates.Incident;
import com.codenova.vigilia.incidents.domain.model.commands.ReportIncidentCommand;
import com.codenova.vigilia.incidents.domain.model.commands.UpdateIncidentCommand;
import com.codenova.vigilia.incidents.domain.model.valueobjects.IncidentStatus;
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
    public Result<Incident, IncidentCommandFailure> handle(UpdateIncidentCommand command) {
        var result = incidentRepository.findById(command.incidentId());
        if (result.isEmpty()) {
            log.warn("Incident not found: id={}", command.incidentId());
            return Result.failure(new IncidentCommandFailure.NotFound());
        }
        var incident = result.get();
        var wantsRating = command.ratingScore() != null && !incident.isRated();
        if (wantsRating && command.status() != IncidentStatus.RESOLVED) {
            log.warn("Rating not allowed: id={}, status={}", incident.getId(), command.status());
            return Result.failure(new IncidentCommandFailure.RatingNotAllowed());
        }
        if (!incident.changeStatus(command.status())) {
            log.warn("Invalid transition: id={}, from={}, to={}", incident.getId(), incident.getStatus(), command.status());
            return Result.failure(new IncidentCommandFailure.InvalidStatusTransition());
        }
        if (wantsRating) incident.rate(command.ratingScore(), command.ratingComment());
        var updated = incidentRepository.save(incident);
        log.info("Incident updated: id={}, status={}", updated.getId(), updated.getStatus());
        return Result.success(updated);
    }
}