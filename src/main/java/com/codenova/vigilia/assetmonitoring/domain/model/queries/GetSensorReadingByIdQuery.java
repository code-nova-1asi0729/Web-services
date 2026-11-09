package com.codenova.vigilia.assetmonitoring.domain.model.queries;

public record GetSensorReadingByIdQuery(Long readingId) {
    public GetSensorReadingByIdQuery {
        if (readingId == null || readingId <= 0) throw new IllegalArgumentException("reading.error.id.invalid");
    }
}
