package com.codenova.vigilia.assetmonitoring.interfaces.rest.transform;

import com.codenova.vigilia.assetmonitoring.application.commandservices.SensorReadingCommandFailure;
import com.codenova.vigilia.assetmonitoring.domain.model.aggregates.SensorReading;
import com.codenova.vigilia.shared.application.patterns.Result;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

public class ResponseEntityFromSensorReadingCommandResultAssembler {

    public static ResponseEntity<?> toResponseEntityFromResult(
            Result<SensorReading, SensorReadingCommandFailure> result, HttpStatus successStatus,
            MessageSource messageSource) {
        return result.fold(
                reading -> ResponseEntity.status(successStatus)
                        .body(SensorReadingResourceFromEntityAssembler.toResourceFromEntity(reading)),
                failure -> {
                    var status = statusFromFailure(failure);
                    var detail = messageSource.getMessage(
                            failure.messageKey(), null, failure.messageKey(), LocaleContextHolder.getLocale());
                    return ResponseEntity.status(status).body(ProblemDetail.forStatusAndDetail(status, detail));
                });
    }

    private static HttpStatus statusFromFailure(SensorReadingCommandFailure failure) {
        return switch (failure) {
            case SensorReadingCommandFailure.SensorNotFound _ -> HttpStatus.NOT_FOUND;
            case SensorReadingCommandFailure.SensorNotAssigned _ -> HttpStatus.CONFLICT;
        };
    }
}
