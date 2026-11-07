package com.codenova.vigilia.incidents.interfaces.rest.transform;

import com.codenova.vigilia.incidents.domain.model.aggregates.Incident;
import com.codenova.vigilia.incidents.interfaces.rest.resources.IncidentResource;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

import java.util.List;
/**
 * Maps incident query results to HTTP responses
 */
public class ResponseEntityFromIncidentQueryResultAssembler {

    public static ResponseEntity<IncidentResource> toResponseEntityFromEntity(Incident incident) {
        return ResponseEntity.ok(IncidentResourceFromEntityAssembler.toResourceFromEntity(incident));
    }

    public static ResponseEntity<List<IncidentResource>> toResponseEntityFromList(List<Incident> incidents) {
        var resources = incidents.stream()
                .map(IncidentResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    public static ResponseEntity<ProblemDetail> notFound(MessageSource messageSource, String messageKey, Object... args) {
        var detail = messageSource.getMessage(messageKey, args, messageKey, LocaleContextHolder.getLocale());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, detail));
    }
}