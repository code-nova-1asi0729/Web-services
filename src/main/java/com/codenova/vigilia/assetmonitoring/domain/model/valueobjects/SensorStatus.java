package com.codenova.vigilia.assetmonitoring.domain.model.valueobjects;

/**
 * Connection status of an IoT sensor.
 */
public enum SensorStatus {
    UNASSIGNED,
    ASSIGNED,
    MONITORING_ACTIVE,
    DISCONNECTED,
    INACTIVE
}
