package com.codenova.vigilia.assetmonitoring.domain.model.valueobjects;

/**
 * Serial number printed on an IoT sensor. It is unique in the whole platform.
 */
public record SerialNumber(String value) {

    private static final String PATTERN = "^[A-Z0-9-]{3,30}$";

    public SerialNumber {
        if (value == null || value.isBlank()) throw new IllegalArgumentException("sensor.error.serialNumber.notBlank");
        if (!value.matches(PATTERN)) throw new IllegalArgumentException("sensor.error.serialNumber.pattern");
    }
}
