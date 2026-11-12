package com.codenova.vigilia.assetmonitoring.interfaces.rest;

import com.codenova.vigilia.assetmonitoring.application.commandservices.DeviceCommandService;
import com.codenova.vigilia.assetmonitoring.application.queryservices.DeviceQueryService;
import com.codenova.vigilia.assetmonitoring.domain.model.queries.GetAllDevicesQuery;
import com.codenova.vigilia.assetmonitoring.domain.model.queries.GetDevicesByBuildingIdQuery;
import com.codenova.vigilia.assetmonitoring.domain.model.queries.GetDeviceByIdQuery;
import com.codenova.vigilia.assetmonitoring.interfaces.rest.resources.RegisterDeviceResource;
import com.codenova.vigilia.assetmonitoring.interfaces.rest.resources.DeviceResource;
import com.codenova.vigilia.assetmonitoring.interfaces.rest.resources.ModifyDeviceResource;
import com.codenova.vigilia.assetmonitoring.interfaces.rest.transform.RegisterDeviceCommandFromResourceAssembler;
import com.codenova.vigilia.assetmonitoring.interfaces.rest.transform.ResponseEntityFromDeviceCommandResultAssembler;
import com.codenova.vigilia.assetmonitoring.interfaces.rest.transform.ResponseEntityFromDeviceQueryResultAssembler;
import com.codenova.vigilia.assetmonitoring.interfaces.rest.transform.ModifyDeviceCommandFromResourceAssembler;
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
 * REST endpoints for critical devices.
 */
@RestController
@RequestMapping(value = "/api/v1/devices", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Devices", description = "Device managment endpoints")
public class DevicesController {

    private final DeviceCommandService deviceCommandService;
    private final DeviceQueryService deviceQueryService;
    private final MessageSource messageSource;

    public DevicesController(DeviceCommandService deviceCommandService,
                             DeviceQueryService deviceQueryService,
                             MessageSource messageSource) {
        this.deviceCommandService = deviceCommandService;
        this.deviceQueryService = deviceQueryService;
        this.messageSource = messageSource;
    }

    @Operation(summary = "Register a critical device")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Device registered",
                    content = @Content(schema = @Schema(implementation = DeviceResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Building not found",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "409", description = "Device code already exists in the building",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping
    public ResponseEntity<?> registerDevice(@Valid @RequestBody RegisterDeviceResource resource) {
        var command = RegisterDeviceCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = deviceCommandService.handle(command);
        return ResponseEntityFromDeviceCommandResultAssembler
                .toResponseEntityFromResult(result, HttpStatus.CREATED, messageSource);
    }

    @Operation(summary = "Get all critical devices, optionally filtered by building")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Devices found",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = DeviceResource.class))))
    })
    @GetMapping
    public ResponseEntity<?> getAllDevices(@RequestParam(required = false) Long buildingId) {
        var devices = buildingId == null
                ? deviceQueryService.handle(new GetAllDevicesQuery())
                : deviceQueryService.handle(new GetDevicesByBuildingIdQuery(buildingId));
        return ResponseEntityFromDeviceQueryResultAssembler.toResponseEntityFromList(devices);
    }

    @Operation(summary = "Get a critical device by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Device found",
                    content = @Content(schema = @Schema(implementation = DeviceResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid device id",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Device not found",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping("/{deviceId}")
    public ResponseEntity<?> getDeviceById(@PathVariable Long deviceId) {
        var device = deviceQueryService.handle(new GetDeviceByIdQuery(deviceId));
        if (device.isEmpty())
            return ResponseEntityFromDeviceQueryResultAssembler.notFound(messageSource,
                    "device.error.notFoundById", deviceId);
        return ResponseEntityFromDeviceQueryResultAssembler.toResponseEntityFromEntity(device.get());
    }

    @Operation(summary = "Update or decommission a critical device")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Device updated",
                    content = @Content(schema = @Schema(implementation = DeviceResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Device not found",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "409", description = "Device is decommissioned",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PutMapping("/{deviceId}")
    public ResponseEntity<?> modifyDevice(@PathVariable Long deviceId,
                                             @Valid @RequestBody ModifyDeviceResource resource) {
        var command = ModifyDeviceCommandFromResourceAssembler.toCommandFromResource(deviceId, resource);
        var result = deviceCommandService.handle(command);
        return ResponseEntityFromDeviceCommandResultAssembler
                .toResponseEntityFromResult(result, HttpStatus.OK, messageSource);
    }
}