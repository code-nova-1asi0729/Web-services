package com.codenova.vigilia.assetmonitoring.interfaces.rest;

import com.codenova.vigilia.assetmonitoring.application.commandservices.BuildingCommandService;
import com.codenova.vigilia.assetmonitoring.application.queryservices.BuildingQueryService;
import com.codenova.vigilia.assetmonitoring.domain.model.queries.GetAllBuildingsQuery;
import com.codenova.vigilia.assetmonitoring.domain.model.queries.GetBuildingByIdQuery;
import com.codenova.vigilia.assetmonitoring.interfaces.rest.resources.BuildingResource;
import com.codenova.vigilia.assetmonitoring.interfaces.rest.resources.CreateBuildingResource;
import com.codenova.vigilia.assetmonitoring.interfaces.rest.transform.CreateBuildingCommandFromResourceAssembler;
import com.codenova.vigilia.assetmonitoring.interfaces.rest.transform.ResponseEntityFromBuildingCommandResultAssembler;
import com.codenova.vigilia.assetmonitoring.interfaces.rest.transform.ResponseEntityFromBuildingQueryResultAssembler;
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
 * REST endpoints for buildings.
 */
@RestController
@RequestMapping(value = "/api/v1/buildings", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Buildings", description = "Building management endpoints")
public class BuildingsController {
    private final BuildingCommandService buildingCommandService;
    private final BuildingQueryService buildingQueryService;
    private final MessageSource messageSource;

    public BuildingsController(BuildingCommandService buildingCommandService,
                               BuildingQueryService buildingQueryService,
                               MessageSource messageSource) {
        this.buildingCommandService = buildingCommandService;
        this.buildingQueryService = buildingQueryService;
        this.messageSource = messageSource;
    }

    @Operation(summary = "Create a building")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Building created",
                    content = @Content(schema = @Schema(implementation = BuildingResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "409", description = "Building code already exists",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping
    public ResponseEntity<?> createBuilding(@Valid @RequestBody CreateBuildingResource resource) {
        var command = CreateBuildingCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = buildingCommandService.handle(command);
        return ResponseEntityFromBuildingCommandResultAssembler.toResponseEntityFromResult(result, HttpStatus.CREATED, messageSource);
    }

    @Operation(summary = "Get all buildings")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Buildings found",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = BuildingResource.class))))
    })
    @GetMapping
    public ResponseEntity<?> getAllBuildings() {
        var buildings = buildingQueryService.handle(new GetAllBuildingsQuery());
        return ResponseEntityFromBuildingQueryResultAssembler.toResponseEntityFromList(buildings);
    }

    @Operation(summary = "Get a building by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Building found",
                    content = @Content(schema = @Schema(implementation = BuildingResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid building id",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Building not found",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping("/{buildingId}")
    public ResponseEntity<?> getBuildingById(@PathVariable Long buildingId) {
        var building = buildingQueryService.handle(new GetBuildingByIdQuery(buildingId));
        if (building.isEmpty())
            return ResponseEntityFromBuildingQueryResultAssembler.notFound(messageSource, "building.error.notFoundById", buildingId);
        return ResponseEntityFromBuildingQueryResultAssembler.toResponseEntityFromEntity(building.get());
    }
}