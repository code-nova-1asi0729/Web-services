package com.codenova.vigilia.assetmonitoring.domain.model.valueobjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DeviceCodeTest {

    @Test
    void acceptsValidCode() {
        assertEquals("LP-WP-01", new DeviceCode("LP-WP-01").value());
    }

    @Test
    void rejectsLowercaseCode() {
        assertThrows(IllegalArgumentException.class, () -> new DeviceCode("lp-wp-01"));
    }
}