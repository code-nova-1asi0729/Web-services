package com.codenova.vigilia.assetmonitoring.interfaces.rest.transform;

import com.codenova.vigilia.assetmonitoring.application.commandservices.SensorCommandFailure;
import com.codenova.vigilia.assetmonitoring.domain.model.aggregates.Sensor;
import com.codenova.vigilia.shared.application.patterns.Result;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

public class ResponseEntityFromSensorCommandResultAssembler {

    public static ResponseEntity<?> toResponseEntityFromResult(
            Result<Sensor, SensorCommandFailure> result, HttpStatus successStatus, MessageSource messageSource) {
        return result.fold(
                sensor -> ResponseEntity.status(successStatus)
                        .body(SensorResourceFromEntityAssembler.toResourceFromEntity(sensor)),
                failure -> {
                    var status = statusFromFailure(failure);
                    var detail = messageSource.getMessage(
                            failure.messageKey(), null, failure.messageKey(), LocaleContextHolder.getLocale());
                    return ResponseEntity.status(status).body(ProblemDetail.forStatusAndDetail(status, detail));
                });
    }

    private static HttpStatus statusFromFailure(SensorCommandFailure failure) {
        return switch (failure) {
            case SensorCommandFailure.DuplicateSerialNumber _ -> HttpStatus.CONFLICT;
            case SensorCommandFailure.NotFound _ -> HttpStatus.NOT_FOUND;
            case SensorCommandFailure.EquipmentNotFound _ -> HttpStatus.NOT_FOUND;
            case SensorCommandFailure.EquipmentDecommissioned _ -> HttpStatus.CONFLICT;
            case SensorCommandFailure.AlreadyAssigned _ -> HttpStatus.CONFLICT;
            case SensorCommandFailure.NotAssigned _ -> HttpStatus.CONFLICT;
        };
    }
}
