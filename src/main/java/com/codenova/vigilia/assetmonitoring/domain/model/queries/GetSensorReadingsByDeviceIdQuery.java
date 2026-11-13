package com.codenova.vigilia.assetmonitoring.domain.model.queries;

public record GetSensorReadingsByDeviceIdQuery(Long deviceId) {
    public GetSensorReadingsByDeviceIdQuery {
        if (deviceId == null || deviceId <= 0) throw new IllegalArgumentException("reading.error.deviceId.invalid");
    }
}
