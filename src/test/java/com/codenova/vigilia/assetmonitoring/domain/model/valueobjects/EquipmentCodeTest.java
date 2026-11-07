package com.codenova.vigilia.assetmonitoring.domain.model.valueobjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EquipmentCodeTest {

    @Test
    void acceptsValidCode() {
        assertEquals("LP-WP-01", new EquipmentCode("LP-WP-01").value());
    }

    @Test
    void rejectsLowercaseCode() {
        assertThrows(IllegalArgumentException.class, () -> new EquipmentCode("lp-wp-01"));
    }
}