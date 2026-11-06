package com.codenova.vigilia.assetmonitoring.interfaces.rest.transform;

import com.codenova.vigilia.assetmonitoring.domain.model.aggregates.CriticalEquipment;
import com.codenova.vigilia.assetmonitoring.interfaces.rest.resources.EquipmentResource;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class ResponseEntityFromEquipmentQueryResultAssembler {

    public static ResponseEntity<EquipmentResource> toResponseEntityFromEntity(CriticalEquipment equipment) {
        return ResponseEntity.ok(EquipmentResourceFromEntityAssembler.toResourceFromEntity(equipment));
    }

    public static ResponseEntity<List<EquipmentResource>> toResponseEntityFromList(List<CriticalEquipment> equipment) {
        var resources = equipment.stream()
                .map(EquipmentResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    public static ResponseEntity<ProblemDetail> notFound(MessageSource messageSource, String messageKey, Object... args) {
        var detail = messageSource.getMessage(messageKey, args, messageKey, LocaleContextHolder.getLocale());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, detail));
    }
}