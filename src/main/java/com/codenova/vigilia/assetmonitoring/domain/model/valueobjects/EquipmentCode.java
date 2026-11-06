package com.codenova.vigilia.assetmonitoring.domain.model.valueobjects;

public record EquipmentCode(String value) {

    private static final String PATTERN = "^[A-Z0-9-]{2,30}$";

    public EquipmentCode {
        if (value == null || value.isBlank()) throw new IllegalArgumentException("equipment.error.code.notBlank");
        if (!value.matches(PATTERN)) throw new IllegalArgumentException("equipment.error.code.pattern");
    }
}