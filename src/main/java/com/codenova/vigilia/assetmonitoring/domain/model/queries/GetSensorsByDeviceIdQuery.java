package com.codenova.vigilia.assetmonitoring.domain.model.queries;

public record GetSensorsByDeviceIdQuery(Long deviceId) {
    public GetSensorsByDeviceIdQuery {
        if (deviceId == null || deviceId <= 0) throw new IllegalArgumentException("sensor.error.deviceId.invalid");
    }
}
