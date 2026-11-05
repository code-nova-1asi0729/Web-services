package com.codenova.vigilia.incidents.interfaces.rest.resources;

import com.codenova.vigilia.incidents.domain.model.valueobjects.IncidentCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
/**
 * Request body to report an incident
 */
@Schema(description = "Request to report an incident")
public record ReportIncidentResource(
        @Schema(description = "Building where the incident happens", example = "1")
        @NotNull(message = "{incident.error.buildingId.invalid}")
        @Positive(message = "{incident.error.buildingId.invalid}")
        Long buildingId,

        @Schema(description = "Resident who reports the incident", example = "3")
        @NotNull(message = "{incident.error.residentId.invalid}")
        @Positive(message = "{incident.error.residentId.invalid}")
        Long residentId,

        @Schema(description = "Related critical equipment, optional", example = "3")
        @Positive(message = "{incident.error.equipmentId.invalid}")
        Long equipmentId,

        @Schema(description = "Type of problem", example = "ELEVATOR")
        @NotNull(message = "{incident.error.category.notNull}")
        IncidentCategory category,

        @Schema(description = "What is happening", example = "El ascensor de la torre A hace un ruido fuerte al subir.")
        @NotBlank(message = "{incident.error.description.size}")
        @Size(min = 10, max = 500, message = "{incident.error.description.size}")
        String description) {}