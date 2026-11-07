package com.codenova.vigilia.assetmonitoring.interfaces.rest.transform;

import com.codenova.vigilia.assetmonitoring.domain.model.aggregates.Alert;
import com.codenova.vigilia.assetmonitoring.interfaces.rest.resources.AlertResource;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

import java.util.List;
/**
 * Maps alert query results to HTTP responses.
 */
public class ResponseEntityFromAlertQueryResultAssembler {

    public static ResponseEntity<AlertResource> toResponseEntityFromEntity(Alert alert) {
        return ResponseEntity.ok(AlertResourceFromEntityAssembler.toResourceFromEntity(alert));
    }

    public static ResponseEntity<List<AlertResource>> toResponseEntityFromList(List<Alert> alerts) {
        var resources = alerts.stream()
                .map(AlertResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    public static ResponseEntity<ProblemDetail> notFound(MessageSource messageSource, String messageKey, Object... args) {
        var detail = messageSource.getMessage(messageKey, args, messageKey, LocaleContextHolder.getLocale());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, detail));
    }
}
