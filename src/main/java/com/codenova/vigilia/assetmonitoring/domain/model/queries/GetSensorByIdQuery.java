package com.codenova.vigilia.assetmonitoring.domain.model.queries;

public record GetSensorByIdQuery(Long sensorId) {
    public GetSensorByIdQuery {
        if (sensorId == null || sensorId <= 0) throw new IllegalArgumentException("sensor.error.id.invalid");
    }
}
