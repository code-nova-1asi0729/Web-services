package com.codenova.vigilia.assetmonitoring.interfaces.rest.transform;

import com.codenova.vigilia.assetmonitoring.application.commandservices.DeviceCommandFailure;
import com.codenova.vigilia.assetmonitoring.domain.model.aggregates.Device;
import com.codenova.vigilia.shared.application.patterns.Result;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

public class ResponseEntityFromDeviceCommandResultAssembler {

    public static ResponseEntity<?> toResponseEntityFromResult(
            Result<Device, DeviceCommandFailure> result, HttpStatus successStatus,
            MessageSource messageSource) {
        return result.fold(
                device -> ResponseEntity.status(successStatus)
                        .body(DeviceResourceFromEntityAssembler.toResourceFromEntity(device)),
                failure -> {
                    var status = statusFromFailure(failure);
                    var detail = messageSource.getMessage(
                            failure.messageKey(), null, failure.messageKey(), LocaleContextHolder.getLocale());
                    return ResponseEntity.status(status).body(ProblemDetail.forStatusAndDetail(status, detail));
                });
    }

    private static HttpStatus statusFromFailure(DeviceCommandFailure failure) {
        return switch (failure) {
            case DeviceCommandFailure.DuplicateCode _ -> HttpStatus.CONFLICT;
            case DeviceCommandFailure.NotFound _ -> HttpStatus.NOT_FOUND;
            case DeviceCommandFailure.BuildingNotFound _ -> HttpStatus.NOT_FOUND;
            case DeviceCommandFailure.AlreadyDecommissioned _ -> HttpStatus.CONFLICT;
        };
    }
}