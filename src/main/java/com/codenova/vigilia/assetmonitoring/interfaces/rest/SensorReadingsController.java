package com.codenova.vigilia.assetmonitoring.interfaces.rest;

import com.codenova.vigilia.assetmonitoring.application.commandservices.SensorReadingCommandService;
import com.codenova.vigilia.assetmonitoring.application.queryservices.SensorReadingQueryService;
import com.codenova.vigilia.assetmonitoring.domain.model.queries.GetAllSensorReadingsQuery;
import com.codenova.vigilia.assetmonitoring.domain.model.queries.GetSensorReadingByIdQuery;
import com.codenova.vigilia.assetmonitoring.domain.model.queries.GetSensorReadingsByEquipmentIdQuery;
import com.codenova.vigilia.assetmonitoring.interfaces.rest.resources.RecordReadingResource;
import com.codenova.vigilia.assetmonitoring.interfaces.rest.resources.SensorReadingResource;
import com.codenova.vigilia.assetmonitoring.interfaces.rest.transform.RecordReadingCommandFromResourceAssembler;
import com.codenova.vigilia.assetmonitoring.interfaces.rest.transform.ResponseEntityFromSensorReadingCommandResultAssembler;
import com.codenova.vigilia.assetmonitoring.interfaces.rest.transform.ResponseEntityFromSensorReadingQueryResultAssembler;
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
 * REST endpoints for sensor readings.
 */
@RestController
@RequestMapping(value = "/api/v1/sensor-readings", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Sensor Readings", description = "Sensor reading recording and history endpoints")
public class SensorReadingsController {

    private final SensorReadingCommandService sensorReadingCommandService;
    private final SensorReadingQueryService sensorReadingQueryService;
    private final MessageSource messageSource;

    public SensorReadingsController(SensorReadingCommandService sensorReadingCommandService,
                                    SensorReadingQueryService sensorReadingQueryService,
                                    MessageSource messageSource) {
        this.sensorReadingCommandService = sensorReadingCommandService;
        this.sensorReadingQueryService = sensorReadingQueryService;
        this.messageSource = messageSource;
    }

    @Operation(summary = "Record a sensor reading")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Reading recorded",
                    content = @Content(schema = @Schema(implementation = SensorReadingResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Sensor not found",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "409", description = "Sensor is not assigned to an equipment",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping
    public ResponseEntity<?> recordReading(@Valid @RequestBody RecordReadingResource resource) {
        var command = RecordReadingCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = sensorReadingCommandService.handle(command);
        return ResponseEntityFromSensorReadingCommandResultAssembler.toResponseEntityFromResult(result, HttpStatus.CREATED, messageSource);
    }

    @Operation(summary = "Get sensor readings, the most recent first, optionally filtered by equipment")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Readings found",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = SensorReadingResource.class))))
    })
    @GetMapping
    public ResponseEntity<?> getAllSensorReadings(@RequestParam(required = false) Long equipmentId) {
        var readings = equipmentId == null
                ? sensorReadingQueryService.handle(new GetAllSensorReadingsQuery())
                : sensorReadingQueryService.handle(new GetSensorReadingsByEquipmentIdQuery(equipmentId));
        return ResponseEntityFromSensorReadingQueryResultAssembler.toResponseEntityFromList(readings);
    }

    @Operation(summary = "Get a sensor reading by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reading found",
                    content = @Content(schema = @Schema(implementation = SensorReadingResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid reading id",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Reading not found",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping("/{readingId}")
    public ResponseEntity<?> getSensorReadingById(@PathVariable Long readingId) {
        var reading = sensorReadingQueryService.handle(new GetSensorReadingByIdQuery(readingId));
        if (reading.isEmpty())
            return ResponseEntityFromSensorReadingQueryResultAssembler.notFound(messageSource, "reading.error.notFoundById", readingId);
        return ResponseEntityFromSensorReadingQueryResultAssembler.toResponseEntityFromEntity(reading.get());
    }
}
