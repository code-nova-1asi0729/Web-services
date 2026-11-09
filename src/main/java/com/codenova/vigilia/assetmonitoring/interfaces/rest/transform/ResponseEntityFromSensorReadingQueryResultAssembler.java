package com.codenova.vigilia.assetmonitoring.interfaces.rest.transform;

import com.codenova.vigilia.assetmonitoring.domain.model.aggregates.SensorReading;
import com.codenova.vigilia.assetmonitoring.interfaces.rest.resources.SensorReadingResource;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class ResponseEntityFromSensorReadingQueryResultAssembler {

    public static ResponseEntity<SensorReadingResource> toResponseEntityFromEntity(SensorReading reading) {
        return ResponseEntity.ok(SensorReadingResourceFromEntityAssembler.toResourceFromEntity(reading));
    }

    public static ResponseEntity<List<SensorReadingResource>> toResponseEntityFromList(List<SensorReading> readings) {
        var resources = readings.stream()
                .map(SensorReadingResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    public static ResponseEntity<ProblemDetail> notFound(MessageSource messageSource, String messageKey, Object... args) {
        var detail = messageSource.getMessage(messageKey, args, messageKey, LocaleContextHolder.getLocale());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, detail));
    }
}
