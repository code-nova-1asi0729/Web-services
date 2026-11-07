package com.codenova.vigilia.assetmonitoring.interfaces.rest.transform;

import com.codenova.vigilia.assetmonitoring.domain.model.aggregates.Building;
import com.codenova.vigilia.assetmonitoring.interfaces.rest.resources.BuildingResource;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

import java.util.List;
/**
 * Maps building query results to HTTP responses.
 */
public class ResponseEntityFromBuildingQueryResultAssembler {

    public static ResponseEntity<BuildingResource> toResponseEntityFromEntity(Building building) {
        return ResponseEntity.ok(BuildingResourceFromEntityAssembler.toResourceFromEntity(building));
    }

    public static ResponseEntity<List<BuildingResource>> toResponseEntityFromList(List<Building> buildings) {
        var resources = buildings.stream()
                .map(BuildingResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    public static ResponseEntity<ProblemDetail> notFound(MessageSource messageSource, String messageKey, Object... args) {
        var detail = messageSource.getMessage(messageKey, args, messageKey, LocaleContextHolder.getLocale());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, detail));
    }
}