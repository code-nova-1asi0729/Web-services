package com.codenova.vigilia.assetmonitoring.domain.model.valueobjects;

public record DeviceCode(String value) {

    private static final String PATTERN = "^[A-Z0-9-]{2,30}$";

    public DeviceCode {
        if (value == null || value.isBlank()) throw new IllegalArgumentException("device.error.code.notBlank");
        if (!value.matches(PATTERN)) throw new IllegalArgumentException("device.error.code.pattern");
    }
}