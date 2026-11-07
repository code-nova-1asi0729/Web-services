package com.codenova.vigilia.assetmonitoring.domain.model.valueobjects;
/**
 * Unique code that identifies a building, such as LP-01.
 */
public record BuildingCode(String value) {
    private static final String PATTERN = "^[A-Z0-9-]{2,20}$";

    public BuildingCode {
        if (value == null || value.isBlank()) throw new IllegalArgumentException("building.error.code.notBlank");
        if (!value.matches(PATTERN)) throw new IllegalArgumentException("building.error.code.pattern");
    }
}