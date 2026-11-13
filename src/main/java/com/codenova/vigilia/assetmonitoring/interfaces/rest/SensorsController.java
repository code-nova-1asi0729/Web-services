package com.codenova.vigilia.assetmonitoring.interfaces.rest;

import com.codenova.vigilia.assetmonitoring.application.commandservices.SensorCommandService;
import com.codenova.vigilia.assetmonitoring.application.queryservices.SensorQueryService;
import com.codenova.vigilia.assetmonitoring.domain.model.commands.ReleaseSensorCommand;
import com.codenova.vigilia.assetmonitoring.domain.model.queries.GetAllSensorsQuery;
import com.codenova.vigilia.assetmonitoring.domain.model.queries.GetSensorByIdQuery;
import com.codenova.vigilia.assetmonitoring.domain.model.queries.GetSensorsByDeviceIdQuery;
import com.codenova.vigilia.assetmonitoring.interfaces.rest.resources.AssignSensorResource;
import com.codenova.vigilia.assetmonitoring.interfaces.rest.resources.RegisterSensorResource;
import com.codenova.vigilia.assetmonitoring.interfaces.rest.resources.SensorResource;
import com.codenova.vigilia.assetmonitoring.interfaces.rest.transform.AssignSensorCommandFromResourceAssembler;
import com.codenova.vigilia.assetmonitoring.interfaces.rest.transform.RegisterSensorCommandFromResourceAssembler;
import com.codenova.vigilia.assetmonitoring.interfaces.rest.transform.ResponseEntityFromSensorCommandResultAssembler;
import com.codenova.vigilia.assetmonitoring.interfaces.rest.transform.ResponseEntityFromSensorQueryResultAssembler;
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
 * REST endpoints for IoT sensors.
 */
@RestController
@RequestMapping(value = "/api/v1/sensors", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Sensors", description = "IoT sensor registration and assignment endpoints")
public class SensorsController {

    private final SensorCommandService sensorCommandService;
    private final SensorQueryService sensorQueryService;
    private final MessageSource messageSource;

    public SensorsController(SensorCommandService sensorCommandService, SensorQueryService sensorQueryService,
                             MessageSource messageSource) {
        this.sensorCommandService = sensorCommandService;
        this.sensorQueryService = sensorQueryService;
        this.messageSource = messageSource;
    }

    @Operation(summary = "Register an IoT sensor")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Sensor registered",
                    content = @Content(schema = @Schema(implementation = SensorResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "409", description = "Serial number already exists",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping
    public ResponseEntity<?> registerSensor(@Valid @RequestBody RegisterSensorResource resource) {
        var command = RegisterSensorCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = sensorCommandService.handle(command);
        return ResponseEntityFromSensorCommandResultAssembler.toResponseEntityFromResult(result, HttpStatus.CREATED, messageSource);
    }

    @Operation(summary = "Get all sensors, optionally filtered by device")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sensors found",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = SensorResource.class))))
    })
    @GetMapping
    public ResponseEntity<?> getAllSensors(@RequestParam(required = false) Long deviceId) {
        var sensors = deviceId == null
                ? sensorQueryService.handle(new GetAllSensorsQuery())
                : sensorQueryService.handle(new GetSensorsByDeviceIdQuery(deviceId));
        return ResponseEntityFromSensorQueryResultAssembler.toResponseEntityFromList(sensors);
    }

    @Operation(summary = "Get a sensor by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sensor found",
                    content = @Content(schema = @Schema(implementation = SensorResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid sensor id",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Sensor not found",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping("/{sensorId}")
    public ResponseEntity<?> getSensorById(@PathVariable Long sensorId) {
        var sensor = sensorQueryService.handle(new GetSensorByIdQuery(sensorId));
        if (sensor.isEmpty())
            return ResponseEntityFromSensorQueryResultAssembler.notFound(messageSource, "sensor.error.notFoundById", sensorId);
        return ResponseEntityFromSensorQueryResultAssembler.toResponseEntityFromEntity(sensor.get());
    }

    @Operation(summary = "Assign a sensor to a device")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sensor assigned",
                    content = @Content(schema = @Schema(implementation = SensorResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Sensor or device not found",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "409", description = "Sensor already assigned or device decommissioned",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping("/{sensorId}/assignments")
    public ResponseEntity<?> assignSensor(@PathVariable Long sensorId, @Valid @RequestBody AssignSensorResource resource) {
        var command = AssignSensorCommandFromResourceAssembler.toCommandFromResource(sensorId, resource);
        var result = sensorCommandService.handle(command);
        return ResponseEntityFromSensorCommandResultAssembler.toResponseEntityFromResult(result, HttpStatus.OK, messageSource);
    }

    @Operation(summary = "Release a sensor from its device")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sensor released",
                    content = @Content(schema = @Schema(implementation = SensorResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid sensor id",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Sensor not found",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "409", description = "Sensor is not assigned",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping("/{sensorId}/releases")
    public ResponseEntity<?> releaseSensor(@PathVariable Long sensorId) {
        var result = sensorCommandService.handle(new ReleaseSensorCommand(sensorId));
        return ResponseEntityFromSensorCommandResultAssembler.toResponseEntityFromResult(result, HttpStatus.OK, messageSource);
    }
}
