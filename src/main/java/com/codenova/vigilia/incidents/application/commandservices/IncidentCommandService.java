package com.codenova.vigilia.incidents.application.commandservices;

import com.codenova.vigilia.incidents.domain.model.aggregates.Incident;
import com.codenova.vigilia.incidents.domain.model.commands.RateIncidentCommand;
import com.codenova.vigilia.incidents.domain.model.commands.ReportIncidentCommand;
import com.codenova.vigilia.incidents.domain.model.commands.ResolveIncidentCommand;
import com.codenova.vigilia.incidents.domain.model.commands.StartIncidentManagementCommand;
import com.codenova.vigilia.incidents.domain.model.commands.UpdateIncidentCommand;
import com.codenova.vigilia.shared.application.patterns.Result;
/**
 * Handles commands that change incidents
 */
public interface IncidentCommandService {

    Result<Incident, IncidentCommandFailure> handle(ReportIncidentCommand command);

    Result<Incident, IncidentCommandFailure> handle(UpdateIncidentCommand command);

    Result<Incident, IncidentCommandFailure> handle(StartIncidentManagementCommand command);

    Result<Incident, IncidentCommandFailure> handle(ResolveIncidentCommand command);

    Result<Incident, IncidentCommandFailure> handle(RateIncidentCommand command);
}