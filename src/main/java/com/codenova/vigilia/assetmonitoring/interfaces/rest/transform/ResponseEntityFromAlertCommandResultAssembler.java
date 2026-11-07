package com.codenova.vigilia.assetmonitoring.interfaces.rest.transform;

import com.codenova.vigilia.assetmonitoring.application.commandservices.AlertCommandFailure;
import com.codenova.vigilia.assetmonitoring.domain.model.aggregates.Alert;
import com.codenova.vigilia.shared.application.patterns.Result;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
/**
 * Maps alert command results to HTTP responses.
 */
public class ResponseEntityFromAlertCommandResultAssembler {

    public static ResponseEntity<?> toResponseEntityFromResult(
            Result<Alert, AlertCommandFailure> result, HttpStatus successStatus, MessageSource messageSource) {
        return result.fold(
                alert -> ResponseEntity.status(successStatus)
                        .body(AlertResourceFromEntityAssembler.toResourceFromEntity(alert)),
                failure -> {
                    var status = statusFromFailure(failure);
                    var detail = messageSource.getMessage(
                            failure.messageKey(), null, failure.messageKey(), LocaleContextHolder.getLocale());
                    return ResponseEntity.status(status).body(ProblemDetail.forStatusAndDetail(status, detail));
                });
    }

    private static HttpStatus statusFromFailure(AlertCommandFailure failure) {
        return switch (failure) {
            case AlertCommandFailure.NotFound _ -> HttpStatus.NOT_FOUND;
            case AlertCommandFailure.InvalidStatusTransition _ -> HttpStatus.CONFLICT;
        };
    }
}
