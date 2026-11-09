package com.codenova.vigilia.incidents.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
/**
 * Request body to rate a resolved incident
 */
@Schema(description = "Request to rate a resolved incident")
public record RateIncidentResource(
        @Schema(description = "Score from 1 to 5", example = "5")
        @NotNull(message = "{incident.error.rating.range}")
        @Min(value = 1, message = "{incident.error.rating.range}")
        @Max(value = 5, message = "{incident.error.rating.range}")
        Integer score,

        @Schema(description = "Optional comment of the resident", example = "Lo arreglaron el mismo día.")
        @Size(max = 300, message = "{incident.error.ratingComment.size}")
        String comment) {}
