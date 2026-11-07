package com.codenova.vigilia.assetmonitoring.interfaces.rest;

import com.codenova.vigilia.assetmonitoring.application.commandservices.AlertCommandService;
import com.codenova.vigilia.assetmonitoring.application.queryservices.AlertQueryService;
import com.codenova.vigilia.assetmonitoring.domain.model.commands.AcknowledgeAlertCommand;
import com.codenova.vigilia.assetmonitoring.domain.model.commands.ResolveAlertCommand;
import com.codenova.vigilia.assetmonitoring.domain.model.queries.GetAlertByIdQuery;
import com.codenova.vigilia.assetmonitoring.domain.model.queries.GetAlertsByStatusQuery;
import com.codenova.vigilia.assetmonitoring.domain.model.queries.GetAllAlertsQuery;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.AlertStatus;
import com.codenova.vigilia.assetmonitoring.interfaces.rest.resources.AlertResource;
import com.codenova.vigilia.assetmonitoring.interfaces.rest.resources.RaiseAlertResource;
import com.codenova.vigilia.assetmonitoring.interfaces.rest.transform.RaiseAlertCommandFromResourceAssembler;
import com.codenova.vigilia.assetmonitoring.interfaces.rest.transform.ResponseEntityFromAlertCommandResultAssembler;
import com.codenova.vigilia.assetmonitoring.interfaces.rest.transform.ResponseEntityFromAlertQueryResultAssembler;
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
 * REST endpoints for alerts raised on critical equipment.
 */
@RestController
@RequestMapping(value = "/api/v1/alerts", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Alerts", description = "Alert monitoring and attention endpoints")
public class AlertsController {
    private final AlertCommandService alertCommandService;
    private final AlertQueryService alertQueryService;
    private final MessageSource messageSource;

    public AlertsController(AlertCommandService alertCommandService,
                            AlertQueryService alertQueryService,
                            MessageSource messageSource) {
        this.alertCommandService = alertCommandService;
        this.alertQueryService = alertQueryService;
        this.messageSource = messageSource;
    }

    @Operation(summary = "Raise an alert")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Alert raised",
                    content = @Content(schema = @Schema(implementation = AlertResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping
    public ResponseEntity<?> raiseAlert(@Valid @RequestBody RaiseAlertResource resource) {
        var command = RaiseAlertCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = alertCommandService.handle(command);
        return ResponseEntityFromAlertCommandResultAssembler.toResponseEntityFromResult(result, HttpStatus.CREATED, messageSource);
    }

    @Operation(summary = "Get all alerts, optionally filtered by status")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Alerts found",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = AlertResource.class)))),
            @ApiResponse(responseCode = "400", description = "Invalid status",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping
    public ResponseEntity<?> getAllAlerts(@RequestParam(required = false) AlertStatus status) {
        var alerts = status == null
                ? alertQueryService.handle(new GetAllAlertsQuery())
                : alertQueryService.handle(new GetAlertsByStatusQuery(status));
        return ResponseEntityFromAlertQueryResultAssembler.toResponseEntityFromList(alerts);
    }

    @Operation(summary = "Get an alert by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Alert found",
                    content = @Content(schema = @Schema(implementation = AlertResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid alert id",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Alert not found",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping("/{alertId}")
    public ResponseEntity<?> getAlertById(@PathVariable Long alertId) {
        var alert = alertQueryService.handle(new GetAlertByIdQuery(alertId));
        if (alert.isEmpty())
            return ResponseEntityFromAlertQueryResultAssembler.notFound(messageSource, "alert.error.notFoundById", alertId);
        return ResponseEntityFromAlertQueryResultAssembler.toResponseEntityFromEntity(alert.get());
    }

    @Operation(summary = "Acknowledge an active alert")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Alert acknowledged",
                    content = @Content(schema = @Schema(implementation = AlertResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid alert id",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Alert not found",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "409", description = "Only an active alert can be acknowledged",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping("/{alertId}/acknowledgements")
    public ResponseEntity<?> acknowledgeAlert(@PathVariable Long alertId) {
        var result = alertCommandService.handle(new AcknowledgeAlertCommand(alertId));
        return ResponseEntityFromAlertCommandResultAssembler.toResponseEntityFromResult(result, HttpStatus.OK, messageSource);
    }

    @Operation(summary = "Resolve an alert")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Alert resolved",
                    content = @Content(schema = @Schema(implementation = AlertResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid alert id",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Alert not found",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "409", description = "The alert is already resolved",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping("/{alertId}/resolutions")
    public ResponseEntity<?> resolveAlert(@PathVariable Long alertId) {
        var result = alertCommandService.handle(new ResolveAlertCommand(alertId));
        return ResponseEntityFromAlertCommandResultAssembler.toResponseEntityFromResult(result, HttpStatus.OK, messageSource);
    }
}
