package com.codenova.vigilia.assetmonitoring.interfaces.rest;

import com.codenova.vigilia.assetmonitoring.application.commandservices.EquipmentCommandService;
import com.codenova.vigilia.assetmonitoring.application.queryservices.EquipmentQueryService;
import com.codenova.vigilia.assetmonitoring.domain.model.queries.GetAllEquipmentQuery;
import com.codenova.vigilia.assetmonitoring.domain.model.queries.GetEquipmentByBuildingIdQuery;
import com.codenova.vigilia.assetmonitoring.domain.model.queries.GetEquipmentByIdQuery;
import com.codenova.vigilia.assetmonitoring.interfaces.rest.resources.CreateEquipmentResource;
import com.codenova.vigilia.assetmonitoring.interfaces.rest.resources.EquipmentResource;
import com.codenova.vigilia.assetmonitoring.interfaces.rest.resources.UpdateEquipmentResource;
import com.codenova.vigilia.assetmonitoring.interfaces.rest.transform.CreateEquipmentCommandFromResourceAssembler;
import com.codenova.vigilia.assetmonitoring.interfaces.rest.transform.ResponseEntityFromEquipmentCommandResultAssembler;
import com.codenova.vigilia.assetmonitoring.interfaces.rest.transform.ResponseEntityFromEquipmentQueryResultAssembler;
import com.codenova.vigilia.assetmonitoring.interfaces.rest.transform.UpdateEquipmentCommandFromResourceAssembler;
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
 * REST endpoints for critical equipment.
 */
@RestController
@RequestMapping(value = "/api/v1/equipment", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Equipment", description = "Critical equipment management endpoints")
public class EquipmentController {

    private final EquipmentCommandService equipmentCommandService;
    private final EquipmentQueryService equipmentQueryService;
    private final MessageSource messageSource;

    public EquipmentController(EquipmentCommandService equipmentCommandService,
                               EquipmentQueryService equipmentQueryService,
                               MessageSource messageSource) {
        this.equipmentCommandService = equipmentCommandService;
        this.equipmentQueryService = equipmentQueryService;
        this.messageSource = messageSource;
    }

    @Operation(summary = "Create a critical equipment")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Equipment created",
                    content = @Content(schema = @Schema(implementation = EquipmentResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Building not found",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "409", description = "Equipment code already exists in the building",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping
    public ResponseEntity<?> createEquipment(@Valid @RequestBody CreateEquipmentResource resource) {
        var command = CreateEquipmentCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = equipmentCommandService.handle(command);
        return ResponseEntityFromEquipmentCommandResultAssembler
                .toResponseEntityFromResult(result, HttpStatus.CREATED, messageSource);
    }

    @Operation(summary = "Get all critical equipment, optionally filtered by building")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Equipment found",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = EquipmentResource.class))))
    })
    @GetMapping
    public ResponseEntity<?> getAllEquipment(@RequestParam(required = false) Long buildingId) {
        var equipment = buildingId == null
                ? equipmentQueryService.handle(new GetAllEquipmentQuery())
                : equipmentQueryService.handle(new GetEquipmentByBuildingIdQuery(buildingId));
        return ResponseEntityFromEquipmentQueryResultAssembler.toResponseEntityFromList(equipment);
    }

    @Operation(summary = "Get a critical equipment by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Equipment found",
                    content = @Content(schema = @Schema(implementation = EquipmentResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid equipment id",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Equipment not found",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping("/{equipmentId}")
    public ResponseEntity<?> getEquipmentById(@PathVariable Long equipmentId) {
        var equipment = equipmentQueryService.handle(new GetEquipmentByIdQuery(equipmentId));
        if (equipment.isEmpty())
            return ResponseEntityFromEquipmentQueryResultAssembler.notFound(messageSource,
                    "equipment.error.notFoundById", equipmentId);
        return ResponseEntityFromEquipmentQueryResultAssembler.toResponseEntityFromEntity(equipment.get());
    }

    @Operation(summary = "Update or decommission a critical equipment")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Equipment updated",
                    content = @Content(schema = @Schema(implementation = EquipmentResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Equipment not found",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "409", description = "Equipment is decommissioned",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PutMapping("/{equipmentId}")
    public ResponseEntity<?> updateEquipment(@PathVariable Long equipmentId,
                                             @Valid @RequestBody UpdateEquipmentResource resource) {
        var command = UpdateEquipmentCommandFromResourceAssembler.toCommandFromResource(equipmentId, resource);
        var result = equipmentCommandService.handle(command);
        return ResponseEntityFromEquipmentCommandResultAssembler
                .toResponseEntityFromResult(result, HttpStatus.OK, messageSource);
    }
}