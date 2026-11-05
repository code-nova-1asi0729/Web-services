package com.codenova.vigilia.incidents.interfaces.rest.transform;

import com.codenova.vigilia.incidents.application.commandservices.IncidentCommandFailure;
import com.codenova.vigilia.incidents.domain.model.aggregates.Incident;
import com.codenova.vigilia.shared.application.patterns.Result;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
/**
 * Maps incident command results to HTTP responses
 */
public class ResponseEntityFromIncidentCommandResultAssembler {

    public static ResponseEntity<?> toResponseEntityFromResult(
            Result<Incident, IncidentCommandFailure> result, HttpStatus successStatus, MessageSource messageSource) {
        return result.fold(
                incident -> ResponseEntity.status(successStatus)
                        .body(IncidentResourceFromEntityAssembler.toResourceFromEntity(incident)),
                failure -> {
                    var status = statusFromFailure(failure);
                    var detail = messageSource.getMessage(
                            failure.messageKey(), null, failure.messageKey(), LocaleContextHolder.getLocale());
                    return ResponseEntity.status(status).body(ProblemDetail.forStatusAndDetail(status, detail));
                });
    }

    private static HttpStatus statusFromFailure(IncidentCommandFailure failure) {
        return switch (failure) {
            case IncidentCommandFailure.NotFound _ -> HttpStatus.NOT_FOUND;
            case IncidentCommandFailure.InvalidStatusTransition _ -> HttpStatus.CONFLICT;
            case IncidentCommandFailure.RatingNotAllowed _ -> HttpStatus.CONFLICT;
        };
    }
}