package com.codenova.vigilia.incidents.interfaces.rest.resources;

import com.codenova.vigilia.incidents.domain.model.valueobjects.IncidentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
/**
 * Request body to change the status of an incident and, optionally, rate it
 */
@Schema(description = "Request to update an incident")
public record UpdateIncidentResource(
        @Schema(description = "New status", example = "RESOLVED")
        @NotNull(message = "{incident.error.status.notNull}")
        IncidentStatus status,

        @Schema(description = "Score from 1 to 5, only for resolved incidents", example = "5")
        @Min(value = 1, message = "{incident.error.rating.range}")
        @Max(value = 5, message = "{incident.error.rating.range}")
        Integer ratingScore,

        @Schema(description = "Optional comment of the rating", example = "Lo arreglaron el mismo día.")
        @Size(max = 300, message = "{incident.error.ratingComment.size}")
        String ratingComment) {}