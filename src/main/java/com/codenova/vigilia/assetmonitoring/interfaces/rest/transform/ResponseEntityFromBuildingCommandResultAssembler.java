package com.codenova.vigilia.assetmonitoring.interfaces.rest.transform;

import com.codenova.vigilia.assetmonitoring.application.commandservices.BuildingCommandFailure;
import com.codenova.vigilia.assetmonitoring.domain.model.aggregates.Building;
import com.codenova.vigilia.shared.application.patterns.Result;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
/**
 * Maps building command results to HTTP responses.
 */
public class ResponseEntityFromBuildingCommandResultAssembler {

    public static ResponseEntity<?> toResponseEntityFromResult(
            Result<Building, BuildingCommandFailure> result, HttpStatus successStatus, MessageSource messageSource) {
        return result.fold(
                building -> ResponseEntity.status(successStatus)
                        .body(BuildingResourceFromEntityAssembler.toResourceFromEntity(building)),
                failure -> toProblemResponse(failure, messageSource));
    }

    private static ResponseEntity<?> toProblemResponse(BuildingCommandFailure failure, MessageSource messageSource) {
        var status = statusFromFailure(failure);
        var detail = messageSource.getMessage(
                failure.messageKey(), null, failure.messageKey(), LocaleContextHolder.getLocale());
        return ResponseEntity.status(status).body(ProblemDetail.forStatusAndDetail(status, detail));
    }

    private static HttpStatus statusFromFailure(BuildingCommandFailure failure) {
        return switch (failure) {
            case BuildingCommandFailure.DuplicateCode _ -> HttpStatus.CONFLICT;
            case BuildingCommandFailure.NotFound _ -> HttpStatus.NOT_FOUND;
        };
    }
}