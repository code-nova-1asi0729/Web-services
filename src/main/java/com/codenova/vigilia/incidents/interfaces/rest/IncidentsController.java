package com.codenova.vigilia.incidents.interfaces.rest;

import com.codenova.vigilia.incidents.application.commandservices.IncidentCommandService;
import com.codenova.vigilia.incidents.application.queryservices.IncidentQueryService;
import com.codenova.vigilia.incidents.domain.model.queries.GetAllIncidentsQuery;
import com.codenova.vigilia.incidents.domain.model.queries.GetIncidentByIdQuery;
import com.codenova.vigilia.incidents.domain.model.queries.GetIncidentsByBuildingIdQuery;
import com.codenova.vigilia.incidents.interfaces.rest.resources.IncidentResource;
import com.codenova.vigilia.incidents.interfaces.rest.resources.ReportIncidentResource;
import com.codenova.vigilia.incidents.interfaces.rest.resources.UpdateIncidentResource;
import com.codenova.vigilia.incidents.interfaces.rest.transform.ReportIncidentCommandFromResourceAssembler;
import com.codenova.vigilia.incidents.interfaces.rest.transform.ResponseEntityFromIncidentCommandResultAssembler;
import com.codenova.vigilia.incidents.interfaces.rest.transform.ResponseEntityFromIncidentQueryResultAssembler;
import com.codenova.vigilia.incidents.interfaces.rest.transform.UpdateIncidentCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
/**
 * REST endpoints for incidents reported by residents
 */
@RestController
@RequestMapping(value = "/api/v1/incidents", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Incidents", description = "Incident reporting and follow-up endpoints")
public class IncidentsController {
    private final IncidentCommandService incidentCommandService;
    private final IncidentQueryService incidentQueryService;
    private final MessageSource messageSource;

    public IncidentsController(IncidentCommandService incidentCommandService,
                               IncidentQueryService incidentQueryService,
                               MessageSource messageSource) {
        this.incidentCommandService = incidentCommandService;
        this.incidentQueryService = incidentQueryService;
        this.messageSource = messageSource;
    }

    @Operation(summary = "Report an incident")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Incident reported",
                    content = @Content(schema = @Schema(implementation = IncidentResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping
    public ResponseEntity<?> reportIncident(@Valid @RequestBody ReportIncidentResource resource) {
        var command = ReportIncidentCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = incidentCommandService.handle(command);
        return ResponseEntityFromIncidentCommandResultAssembler.toResponseEntityFromResult(result, HttpStatus.CREATED, messageSource);
    }

    @Operation(summary = "Get all incidents, optionally filtered by building")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Incidents found",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = IncidentResource.class)))),
            @ApiResponse(responseCode = "400", description = "Invalid building id",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping
    public ResponseEntity<?> getAllIncidents(@RequestParam(required = false) Long buildingId) {
        var incidents = buildingId == null
                ? incidentQueryService.handle(new GetAllIncidentsQuery())
                : incidentQueryService.handle(new GetIncidentsByBuildingIdQuery(buildingId));
        return ResponseEntityFromIncidentQueryResultAssembler.toResponseEntityFromList(incidents);
    }

    @Operation(summary = "Get an incident by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Incident found",
                    content = @Content(schema = @Schema(implementation = IncidentResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid incident id",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Incident not found",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping("/{incidentId}")
    public ResponseEntity<?> getIncidentById(@PathVariable Long incidentId) {
        var incident = incidentQueryService.handle(new GetIncidentByIdQuery(incidentId));
        if (incident.isEmpty())
            return ResponseEntityFromIncidentQueryResultAssembler.notFound(messageSource, "incident.error.notFoundById", incidentId);
        return ResponseEntityFromIncidentQueryResultAssembler.toResponseEntityFromEntity(incident.get());
    }

    @Operation(summary = "Change the status of an incident and, optionally, rate it")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Incident updated",
                    content = @Content(schema = @Schema(implementation = IncidentResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Incident not found",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "409", description = "Invalid status change or rating not allowed",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PutMapping("/{incidentId}")
    public ResponseEntity<?> updateIncident(@PathVariable Long incidentId,
                                            @Valid @RequestBody UpdateIncidentResource resource) {
        var command = UpdateIncidentCommandFromResourceAssembler.toCommandFromResource(incidentId, resource);
        var result = incidentCommandService.handle(command);
        return ResponseEntityFromIncidentCommandResultAssembler.toResponseEntityFromResult(result, HttpStatus.OK, messageSource);
    }
}