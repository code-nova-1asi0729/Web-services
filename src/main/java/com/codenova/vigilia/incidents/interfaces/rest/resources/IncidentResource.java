package com.codenova.vigilia.incidents.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
/**
 * Response body that represents an incident
 */
@Schema(description = "Incident")
public record IncidentResource(
        @Schema(description = "Incident identifier", example = "1")
        Long id,
        @Schema(description = "Building identifier", example = "1")
        Long buildingId,
        @Schema(description = "Resident identifier", example = "3")
        Long residentId,
        @Schema(description = "Critical equipment identifier, may be null", example = "3")
        Long equipmentId,
        @Schema(description = "Type of problem", example = "ELEVATOR")
        String category,
        @Schema(description = "Description", example = "El ascensor de la torre A hace un ruido fuerte al subir.")
        String description,
        @Schema(description = "Attention status", example = "REPORTED")
        String status,
        @Schema(description = "Score from 1 to 5, may be null", example = "5")
        Integer ratingScore,
        @Schema(description = "Rating comment, may be null")
        String ratingComment,
        @Schema(description = "When it was reported", example = "2026-10-06T08:15:00Z")
        Instant reportedAt,
        @Schema(description = "When its attention started, may be null")
        Instant startedAt,
        @Schema(description = "When it was resolved, may be null")
        Instant resolvedAt,
        @Schema(description = "When it was rated, may be null")
        Instant ratedAt) {}