package com.codenova.vigilia.assetmonitoring.interfaces.rest.transform;

import com.codenova.vigilia.assetmonitoring.application.commandservices.EquipmentCommandFailure;
import com.codenova.vigilia.assetmonitoring.domain.model.aggregates.CriticalEquipment;
import com.codenova.vigilia.shared.application.patterns.Result;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

public class ResponseEntityFromEquipmentCommandResultAssembler {

    public static ResponseEntity<?> toResponseEntityFromResult(
            Result<CriticalEquipment, EquipmentCommandFailure> result, HttpStatus successStatus,
            MessageSource messageSource) {
        return result.fold(
                equipment -> ResponseEntity.status(successStatus)
                        .body(EquipmentResourceFromEntityAssembler.toResourceFromEntity(equipment)),
                failure -> {
                    var status = statusFromFailure(failure);
                    var detail = messageSource.getMessage(
                            failure.messageKey(), null, failure.messageKey(), LocaleContextHolder.getLocale());
                    return ResponseEntity.status(status).body(ProblemDetail.forStatusAndDetail(status, detail));
                });
    }

    private static HttpStatus statusFromFailure(EquipmentCommandFailure failure) {
        return switch (failure) {
            case EquipmentCommandFailure.DuplicateCode _ -> HttpStatus.CONFLICT;
            case EquipmentCommandFailure.NotFound _ -> HttpStatus.NOT_FOUND;
            case EquipmentCommandFailure.BuildingNotFound _ -> HttpStatus.NOT_FOUND;
            case EquipmentCommandFailure.AlreadyDecommissioned _ -> HttpStatus.CONFLICT;
        };
    }
}