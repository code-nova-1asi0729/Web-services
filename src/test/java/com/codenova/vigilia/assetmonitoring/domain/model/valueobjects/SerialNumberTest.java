package com.codenova.vigilia.assetmonitoring.domain.model.valueobjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SerialNumberTest {

    @Test
    void acceptsUppercaseLettersDigitsAndHyphens() {
        assertEquals("VS-1001", new SerialNumber("VS-1001").value());
    }

    @Test
    void rejectsBlankOrInvalidValues() {
        assertThrows(IllegalArgumentException.class, () -> new SerialNumber(" "));
        assertThrows(IllegalArgumentException.class, () -> new SerialNumber("vs-1001"));
        assertThrows(IllegalArgumentException.class, () -> new SerialNumber("V1"));
    }
}
