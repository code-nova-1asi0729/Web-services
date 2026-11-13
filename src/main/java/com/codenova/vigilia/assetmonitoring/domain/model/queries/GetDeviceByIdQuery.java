package com.codenova.vigilia.assetmonitoring.domain.model.queries;

public record GetDeviceByIdQuery(Long deviceId) {
    public GetDeviceByIdQuery {
        if(deviceId == null || deviceId <= 0) throw new IllegalArgumentException("device.error.id.invalid");
    }
}
